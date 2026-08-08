package de.jave.jave.algorithm.camel;

import de.jave.image.GImage;
import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.greyscale.GreyscaleImageFactory;
import de.jave.image.monochrome.GMonochromeImage;
import de.jave.jave.algorithm.GeneralAlgorithm;
import de.jave.lib.CharacterPlate;
import de.jave.text.TextTools;
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.PixelGrabber;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Camelizer {
   private final String[] textWords;
   private final String textPure;
   private final String textPreserveSpaces;
   private GGreyscaleImage imageRawInternal;
   private GImage imagePreview;

   public Camelizer(String text) {
      StringBuffer sb = new StringBuffer();

      for (int i = 0; i < text.length(); i++) {
         char ch = text.charAt(i);
         if (ch != ' ' && ch != '\n') {
            sb.append(ch);
         }
      }

      this.textPure = sb.toString();
      sb.setLength(0);
      boolean lastCharSpace = false;

      for (int ix = 0; ix < text.length(); ix++) {
         char ch = text.charAt(ix);
         if (ch == ' ') {
            if (!lastCharSpace) {
               sb.append(' ');
            }

            lastCharSpace = true;
         } else if (ch == '\n') {
            if (!lastCharSpace) {
               sb.append(' ');
            }

            lastCharSpace = true;
         } else {
            sb.append(ch);
            lastCharSpace = false;
         }
      }

      this.textPreserveSpaces = sb.toString();
      StringTokenizer st = new StringTokenizer(this.textPreserveSpaces, String.valueOf(' '), false);
      int count = st.countTokens();
      this.textWords = new String[count];

      for (int ixx = 0; ixx < count; ixx++) {
         this.textWords[ixx] = st.nextToken();
      }
   }

   public void setRawImage(GGreyscaleImage image) {
      if (image.getWidth() > 180) {
         this.imageRawInternal = image.getYScaledInstance(0.5);
      } else {
         this.imageRawInternal = image.getXScaledInstance(2.0);
      }
   }

   public GImage getPreviewImage() {
      return this.imagePreview;
   }

   public boolean checkReady() {
      return this.imageRawInternal != null;
   }

   public CharacterPlate camelize(CamelizeImageOptions imageOptions, CamelizeTextOptions textOptions) {
      switch (textOptions.getPreserveMode()) {
         case PRESERVE_NONE:
            return this.camelizePreserveNone(imageOptions);
         case PRESERVE_WHITESPACE:
            return this.camelizePreserveWhitespace(imageOptions);
         case PRESERVE_WORDS:
            return this.camelizePreserveWords(imageOptions, textOptions.getFillCharacter());
         default:
            throw new IllegalArgumentException();
      }
   }

   private CharacterPlate camelizePreserveWords(CamelizeImageOptions imageOptions, char fillCharacter) {
      int charCount = cellLength(this.textPure);

      WordsToShapeResult result;
      do {
         GMonochromeImage imageShape = getShapeImage(this.imageRawInternal, imageOptions, charCount);
         result = wordsToShape(this.textWords, imageShape, fillCharacter);
         charCount *= 2;
      } while (result.getDifference() < 0);

      charCount /= 2;
      int step = charCount / 4;
      charCount /= 2;

      while (true) {
         GMonochromeImage var11 = getShapeImage(this.imageRawInternal, imageOptions, charCount);
         result = wordsToShape(this.textWords, var11, fillCharacter);
         if (result.getDifference() == 0 || step == 0) {
            if (result.getDifference() < 0) {
               var11 = getShapeImage(this.imageRawInternal, imageOptions, ++charCount);
               result = wordsToShape(this.textWords, var11, fillCharacter);
            }

            this.imagePreview = var11.scaleY2();
            return result.getPlate();
         }

         if (result.getDifference() > 0) {
            charCount -= step;
         } else {
            charCount += step;
         }

         step /= 2;
      }
   }

   private CharacterPlate camelizePreserveWhitespace(CamelizeImageOptions imageOptions) {
      int charCount = cellLength(this.textPreserveSpaces);
      GMonochromeImage imageShape = getShapeImage(this.imageRawInternal, imageOptions, charCount);
      CharacterPlate cp = textToShape(this.textPreserveSpaces, imageShape);
      this.imagePreview = imageShape.scaleY2();
      return cp;
   }

   private CharacterPlate camelizePreserveNone(CamelizeImageOptions imageOptions) {
      int charCount = cellLength(this.textPure);
      GMonochromeImage imageShape = getShapeImage(this.imageRawInternal, imageOptions, charCount);
      CharacterPlate cp = textToShape(this.textPure, imageShape);
      this.imagePreview = imageShape.scaleY2();
      return cp;
   }

   private static CharacterPlate textToShape(String sourceText, GMonochromeImage shapeImage) {
      int[] glyphs = TextTools.toGlyphs(sourceText);
      int charCount = glyphs.length;
      int w = shapeImage.getWidth();
      int h = shapeImage.getHeight();
      CharacterPlate result = new CharacterPlate(w, h);
      int i = 0;

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            if (shapeImage.get(x, y) == 0 && i < charCount) {
               result.set(x, y, glyphs[i++]);
            }
         }
      }

      GeneralAlgorithm.replace(result, ' ', ' ');
      return result;
   }

   private static WordsToShapeResult wordsToShape(String[] words, GMonochromeImage shapeImage, char fillChar) {
      boolean fill = true;
      int w = shapeImage.getWidth();
      int h = shapeImage.getHeight();
      int wordCount = words.length;
      List<CamelRun> runs = new ArrayList<>();

      for (CamelRun run = getNextRun(0, 0, w, h, shapeImage); run != null; run = getNextRun(run.getEndX() + 1, run.getY(), w, h, shapeImage)) {
         runs.add(run);
      }

      int difference = 0;
      int wordIndex = 0;

      for (int i = 0; i < runs.size(); i++) {
         CamelRun var14 = runs.get(i);
         if (wordIndex >= wordCount) {
            difference += var14.getLength();
         } else {
            int length = var14.getLength();
            if (cellLength(words[wordIndex]) <= length) {
               var14.setText(words[wordIndex]);
               length -= cellLength(words[wordIndex]);
               wordIndex++;

               while (wordIndex < wordCount && cellLength(words[wordIndex]) + 1 <= length) {
                  var14.setText(var14.getText() + fillChar + words[wordIndex]);
                  length -= cellLength(words[wordIndex]) + 1;
                  wordIndex++;
               }
            }
         }
      }

      for (int ix = wordIndex; ix < wordCount; ix++) {
         difference -= cellLength(words[ix]);
      }

      CharacterPlate cp = new CharacterPlate(w, h);

      for (int ix = 0; ix < runs.size(); ix++) {
         CamelRun var15 = runs.get(ix);
         if (var15.getText() != null) {
            cp.paste(stretch(var15.getText(), var15.getLength(), fillChar), var15.getStartX(), var15.getY());
         } else {
            for (int j = var15.getStartX(); j <= var15.getEndX(); j++) {
               cp.setForce(j, var15.getY(), fillChar);
            }
         }
      }

      return new WordsToShapeResult(cp, difference);
   }

   private static final String stretch(String text, int length, char fillChar) {
      int diff = length - cellLength(text);
      if (diff == 0) {
         return text;
      } else {
         int i1 = text.indexOf(fillChar);
         if (i1 == -1) {
            return spaces(diff / 2, fillChar) + text + spaces(diff - diff / 2, fillChar);
         } else {
            while (diff > 0) {
               text = text.substring(0, i1) + fillChar + text.substring(i1);
               diff--;
               i1 = text.indexOf(fillChar, i1 + 2);
               if (i1 == -1) {
                  i1 = text.indexOf(fillChar);
               }
            }

            return text;
         }
      }
   }

   private static final String spaces(int length, char fillChar) {
      char[] ch = new char[length];

      for (int i = 0; i < length; i++) {
         ch[i] = fillChar;
      }

      return new String(ch);
   }

   private static int cellLength(String text) {
      return TextTools.toGlyphs(text).length;
   }

   private static CamelRun getNextRun(int x, int y, int w, int h, GMonochromeImage shapeImage) {
      if (x >= w) {
         return getNextRun(0, y + 1, w, h, shapeImage);
      } else if (y >= h) {
         return null;
      } else {
         while (x < w && shapeImage.get(x, y) != 0) {
            x++;
         }

         if (x == w) {
            return getNextRun(0, y + 1, w, h, shapeImage);
         } else {
            int startX = x;

            while (x + 1 < w && shapeImage.get(x + 1, y) == 0) {
               x++;
            }

            return new CamelRun(startX, x, y);
         }
      }
   }

   public static GGreyscaleImage loadImage(Image imageRaw, Component comp) {
      int imageWidth = imageRaw.getWidth(comp);
      int imageHeight = imageRaw.getHeight(comp);
      if (imageHeight > 300 || imageWidth > 400) {
         int newWidth = 0;
         int newHeight = 0;
         if (imageHeight > 300) {
            newHeight = 300;
            newWidth = newHeight * imageWidth / imageHeight;
         } else {
            newWidth = 400;
            newHeight = newWidth * imageHeight / imageWidth;
         }

         imageWidth = newWidth;
         imageHeight = newHeight;
         imageRaw = imageRaw.getScaledInstance(newWidth, newHeight, 2);
         MediaTracker tracker = new MediaTracker(comp);
         tracker.addImage(imageRaw, 0);

         try {
            tracker.waitForID(0);
         } catch (InterruptedException var9) {
         }
      }

      int[] pixels = new int[imageWidth * imageHeight];
      PixelGrabber pg = new PixelGrabber(imageRaw, 0, 0, imageWidth, imageHeight, pixels, 0, imageWidth);

      try {
         pg.grabPixels();
      } catch (InterruptedException var8) {
         throw new RuntimeException("Internal Error: " + var8.toString());
      }

      return new GreyscaleImageFactory().createGreyscaleImage(pixels, imageWidth, imageHeight);
   }

   private static GMonochromeImage getShapeImage(GGreyscaleImage imageRaw, CamelizeImageOptions imageOptions, int charCount) {
      GGreyscaleImage image;
      if (imageOptions.isNegative()) {
         image = imageRaw.getClone();
         image.invert();
      } else {
         image = imageRaw;
      }

      int w = image.getWidth();
      int h = image.getHeight();
      int startThreshold = 128 - (int)(96.0 * imageOptions.getBrightness());
      int startWidth = (int)Math.ceil((double)w * Math.sqrt((double)charCount / (double)image.countPixelsWithMaxValue(startThreshold)));
      int startHeight = h * startWidth / w;
      GGreyscaleImage g3 = null;
      g3 = image.getScaledInstance(startWidth, startHeight);

      int count;
      for (count = g3.countPixelsWithMaxValue(startThreshold); count < charCount; count = g3.countPixelsWithMaxValue(startThreshold)) {
         startWidth++;
         startHeight = h * startWidth / w;
         g3 = image.getScaledInstance(startWidth, startHeight);
      }

      if (count > charCount) {
         int step = 64;
         boolean done = false;

         while (!done) {
            if (count == charCount || step == 0) {
               done = true;
            } else if (count < charCount) {
               startThreshold += step;
               count = g3.countPixelsWithMaxValue(startThreshold);
               step /= 2;
            } else if (count > charCount) {
               startThreshold -= step;
               count = g3.countPixelsWithMaxValue(startThreshold);
               step /= 2;
            }
         }

         if (count < charCount) {
            startThreshold++;
         }
      }

      return g3.getThresholdedInstance(startThreshold);
   }
}
