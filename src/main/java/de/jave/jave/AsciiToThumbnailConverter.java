package de.jave.jave;

import de.jave.ascii.plate.CharacterMetrics;
import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.jave.export.Ascii2ImageOptions;
import de.jave.jave.rendering.ConnectedLinesViewRenderer;
import de.jave.javeplayer.JaveAnimationFile;
import de.jave.javeplayer.JaveAnimationFrame;
import de.jave.lib.CharacterPlate;
import de.jave.text.TextTools;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class AsciiToThumbnailConverter {
   protected static final int[] TABLE_THUMB_1 = new int[]{
      255,
      113,
      98,
      4,
      29,
      98,
      48,
      171,
      117,
      117,
      100,
      98,
      168,
      171,
      188,
      117,
      41,
      74,
      47,
      50,
      32,
      38,
      33,
      85,
      24,
      36,
      131,
      123,
      103,
      85,
      103,
      82,
      7,
      9,
      4,
      43,
      18,
      6,
      20,
      20,
      6,
      52,
      50,
      9,
      38,
      0,
      4,
      39,
      22,
      12,
      10,
      27,
      27,
      26,
      39,
      0,
      22,
      43,
      22,
      85,
      117,
      85,
      163,
      142,
      218,
      35,
      14,
      68,
      14,
      35,
      35,
      11,
      22,
      70,
      60,
      30,
      64,
      6,
      33,
      56,
      7,
      9,
      66,
      47,
      56,
      43,
      58,
      22,
      43,
      32,
      56,
      108,
      117,
      105,
      157
   };
   protected static final int[][] TABLE_THUMB_4 = new int[][]{
      {255, 255, 255, 255},
      {50, 111, 146, 190},
      {50, 50, 255, 255},
      {26, 32, 41, 46},
      {28, 69, 66, 100},
      {69, 146, 151, 146},
      {100, 175, 50, 50},
      {77, 139, 255, 255},
      {175, 100, 151, 100},
      {82, 224, 74, 209},
      {26, 58, 235, 235},
      {43, 86, 97, 146},
      {255, 255, 66, 247},
      {183, 195, 134, 155},
      {255, 255, 146, 171},
      {209, 74, 89, 247},
      {61, 69, 93, 100},
      {47, 157, 58, 107},
      {107, 61, 56, 130},
      {107, 41, 146, 77},
      {97, 47, 97, 41},
      {28, 77, 139, 77},
      {47, 66, 74, 93},
      {111, 43, 190, 139},
      {23, 50, 84, 100},
      {41, 41, 111, 84},
      {131, 162, 146, 171},
      {131, 162, 84, 247},
      {121, 131, 134, 146},
      {111, 121, 126, 134},
      {121, 131, 134, 146},
      {121, 61, 126, 139},
      {18, 21, 50, 58},
      {37, 97, 30, 30},
      {7, 41, 30, 41},
      {54, 86, 74, 130},
      {32, 74, 46, 84},
      {4, 50, 23, 58},
      {21, 21, 46, 146},
      {58, 100, 62, 30},
      {18, 21, 46, 50},
      {43, 92, 58, 107},
      {183, 32, 80, 107},
      {14, 43, 43, 69},
      {37, 209, 53, 66},
      {9, 9, 38, 32},
      {9, 47, 53, 35},
      {69, 77, 80, 93},
      {43, 50, 35, 119},
      {74, 77, 25, 30},
      {18, 43, 30, 77},
      {35, 69, 74, 69},
      {7, 32, 74, 126},
      {50, 50, 80, 89},
      {50, 50, 107, 119},
      {5, 15, 41, 43},
      {43, 61, 56, 80},
      {50, 58, 69, 119},
      {66, 43, 46, 84},
      {82, 100, 74, 100},
      {66, 237, 226, 93},
      {43, 157, 41, 151},
      {117, 131, 255, 255},
      {255, 255, 107, 115},
      {195, 195, 255, 255},
      {151, 127, 32, 38},
      {14, 107, 35, 77},
      {117, 100, 84, 134},
      {97, 18, 69, 41},
      {117, 121, 23, 62},
      {47, 43, 66, 155},
      {100, 69, 30, 14},
      {21, 117, 66, 66},
      {58, 175, 53, 97},
      {100, 92, 146, 56},
      {50, 100, 46, 84},
      {37, 157, 53, 97},
      {26, 77, 9, 30},
      {66, 111, 66, 66},
      {107, 117, 77, 84},
      {47, 107, 0, 77},
      {107, 58, 77, 0},
      {86, 121, 53, 176},
      {107, 127, 58, 58},
      {32, 157, 93, 134},
      {121, 121, 77, 43},
      {92, 92, 107, 107},
      {97, 92, 41, 30},
      {100, 100, 66, 77},
      {111, 100, 18, 74},
      {111, 92, 84, 93},
      {111, 127, 84, 134},
      {82, 157, 74, 151},
      {74, 175, 77, 146},
      {146, 224, 171, 151}
   };
   protected static final int[][] TABLE_THUMB_9 = new int[][]{
      {255, 255, 255, 255, 255, 255, 255, 255, 255, 32},
      {255, 17, 255, 255, 105, 255, 255, 129, 255, 33},
      {14, 0, 23, 164, 168, 195, 255, 255, 255, 34},
      {214, 1, 138, 35, 0, 110, 121, 58, 255, 35},
      {80, 13, 82, 61, 101, 77, 168, 32, 241, 36},
      {104, 129, 255, 156, 113, 150, 255, 158, 202, 37},
      {255, 75, 191, 82, 58, 125, 177, 70, 135, 38},
      {255, 25, 255, 255, 188, 255, 255, 255, 255, 39},
      {255, 91, 212, 255, 99, 255, 255, 83, 227, 40},
      {147, 110, 255, 255, 99, 255, 186, 97, 255, 41},
      {118, 40, 124, 149, 64, 184, 255, 255, 255, 42},
      {255, 152, 255, 149, 52, 158, 255, 207, 255, 43},
      {255, 255, 255, 245, 193, 255, 83, 76, 255, 44},
      {255, 255, 255, 149, 158, 176, 255, 255, 255, 45},
      {255, 255, 255, 255, 198, 255, 255, 58, 255, 46},
      {255, 152, 39, 198, 105, 255, 114, 243, 255, 47},
      {80, 60, 94, 35, 249, 25, 198, 83, 214, 48},
      {132, 60, 255, 255, 99, 255, 147, 58, 156, 49},
      {50, 47, 82, 156, 158, 150, 97, 83, 135, 50},
      {104, 40, 58, 216, 153, 41, 156, 70, 177, 51},
      {255, 5, 124, 71, 28, 84, 255, 97, 156, 52},
      {33, 60, 174, 100, 153, 50, 156, 70, 177, 53},
      {181, 60, 94, 22, 129, 77, 220, 76, 168, 54},
      {50, 40, 23, 255, 105, 184, 255, 177, 255, 55},
      {50, 53, 82, 31, 129, 50, 177, 83, 214, 56},
      {80, 60, 108, 120, 121, 22, 168, 90, 241, 57},
      {255, 255, 255, 255, 83, 255, 255, 58, 255, 58},
      {255, 255, 255, 225, 67, 255, 114, 147, 255, 59},
      {255, 200, 82, 88, 87, 203, 255, 243, 146, 60},
      {255, 255, 255, 51, 83, 61, 255, 255, 255, 61},
      {80, 225, 255, 172, 83, 104, 156, 255, 255, 62},
      {93, 40, 108, 216, 144, 118, 255, 70, 255, 63},
      {40, 47, 47, 35, 94, 33, 147, 76, 168, 64},
      {50, 20, 255, 22, 76, 37, 97, 255, 67, 65},
      {10, 60, 94, 22, 144, 16, 83, 83, 116, 66},
      {58, 53, 30, 25, 255, 118, 168, 83, 156, 67},
      {10, 67, 94, 35, 255, 22, 83, 83, 168, 68},
      {10, 60, 39, 22, 129, 110, 83, 83, 99, 69},
      {33, 40, 4, 35, 64, 195, 114, 83, 255, 70},
      {69, 60, 39, 31, 188, 33, 156, 83, 156, 71},
      {10, 129, 11, 22, 158, 19, 83, 147, 82, 72},
      {93, 25, 108, 255, 99, 255, 147, 58, 156, 73},
      {214, 40, 4, 113, 153, 33, 147, 83, 241, 74},
      {10, 141, 0, 22, 80, 110, 83, 207, 99, 75},
      {40, 47, 255, 120, 99, 125, 114, 58, 82, 76},
      {1, 255, 0, 11, 80, 4, 83, 255, 67, 77},
      {0, 174, 4, 22, 101, 19, 69, 186, 99, 78},
      {104, 60, 124, 31, 255, 25, 168, 83, 191, 79},
      {33, 40, 58, 35, 99, 66, 114, 83, 255, 80},
      {104, 60, 124, 43, 255, 29, 114, 0, 74, 81},
      {10, 53, 108, 22, 113, 37, 83, 207, 127, 82},
      {80, 60, 58, 56, 158, 61, 105, 90, 191, 83},
      {20, 25, 23, 164, 99, 158, 186, 58, 202, 84},
      {3, 174, 4, 31, 249, 22, 168, 76, 177, 85},
      {10, 225, 0, 76, 87, 104, 255, 113, 255, 86},
      {3, 152, 0, 7, 52, 25, 114, 147, 156, 87},
      {10, 141, 23, 82, 64, 158, 83, 207, 82, 88},
      {10, 163, 11, 181, 67, 203, 186, 58, 202, 89},
      {50, 53, 30, 94, 113, 96, 114, 83, 116, 90},
      {255, 36, 174, 255, 99, 255, 255, 37, 202, 91},
      {26, 200, 255, 235, 99, 235, 255, 207, 108, 92},
      {147, 47, 255, 255, 99, 255, 186, 47, 255, 93},
      {163, 47, 255, 172, 220, 184, 255, 255, 255, 94},
      {255, 255, 255, 255, 255, 255, 255, 255, 255, 95},
      {255, 91, 255, 255, 255, 255, 255, 255, 255, 96},
      {255, 255, 255, 51, 47, 55, 121, 76, 99, 97},
      {10, 255, 255, 2, 153, 22, 76, 83, 156, 98},
      {255, 255, 255, 47, 158, 77, 177, 76, 146, 99},
      {255, 200, 11, 25, 140, 1, 147, 83, 74, 100},
      {255, 255, 255, 11, 83, 61, 168, 83, 146, 101},
      {255, 47, 69, 181, 52, 158, 168, 58, 156, 102},
      {255, 255, 255, 35, 129, 2, 97, 2, 48, 103},
      {10, 255, 255, 22, 158, 37, 83, 166, 67, 104},
      {255, 152, 255, 181, 83, 255, 129, 58, 135, 105},
      {255, 152, 255, 149, 64, 255, 147, 17, 255, 106},
      {10, 163, 255, 35, 50, 104, 114, 166, 74, 107},
      {118, 47, 255, 255, 99, 255, 129, 58, 135, 108},
      {255, 255, 255, 6, 64, 19, 83, 113, 99, 109},
      {255, 255, 255, 11, 158, 33, 83, 166, 67, 110},
      {255, 255, 255, 31, 147, 29, 168, 76, 177, 111},
      {255, 255, 255, 0, 147, 22, 0, 41, 156, 112},
      {255, 255, 255, 35, 140, 0, 168, 32, 0, 113},
      {255, 255, 255, 88, 52, 158, 114, 58, 202, 114},
      {255, 255, 255, 76, 80, 50, 129, 83, 156, 115},
      {69, 174, 255, 11, 94, 176, 220, 70, 116, 116},
      {255, 255, 255, 19, 220, 11, 156, 70, 74, 117},
      {255, 255, 255, 22, 113, 13, 255, 97, 255, 118},
      {255, 255, 255, 9, 99, 2, 129, 166, 99, 119},
      {255, 255, 255, 61, 58, 84, 83, 186, 82, 120},
      {255, 255, 255, 51, 105, 25, 129, 2, 255, 121},
      {255, 255, 255, 100, 83, 90, 129, 83, 116, 122},
      {255, 83, 255, 245, 94, 255, 255, 64, 255, 123},
      {255, 91, 255, 255, 99, 255, 255, 83, 255, 124},
      {214, 75, 255, 255, 94, 255, 231, 64, 255, 125},
      {255, 255, 255, 134, 144, 158, 255, 255, 255, 126}
   };

   public static BufferedImage convert(CharacterPlate cpPreview, Ascii2ImageOptions options) {
      return convert(
         cpPreview, options.getFont().getSize(), options.getFont(), options.getBackgroundColor(), options.getForegroundColor(), options.isConnectedLinesView()
      );
   }

   @Deprecated
   public static BufferedImage convert(CharacterPlate cp, int scale, Font font, Color backgroundColor, Color foregroundColor, boolean connectedLinesView) {
      return scale >= 4
         ? createFontRenderedImage(cp, font, backgroundColor, foregroundColor, connectedLinesView)
         : createThumbnailRenderedImage(cp, scale, backgroundColor, foregroundColor);
   }

   private static BufferedImage createThumbnailRenderedImage(CharacterPlate cp, int scale, Color backgroundColor, Color foregroundColor) {
      return ascii2ImageQuick(cp, scale, backgroundColor, foregroundColor);
   }

   private static BufferedImage createFontRenderedImage(CharacterPlate cp, Font font, Color backgroundColor, Color foregroundColor, boolean connectedLinesView) {
      CharacterMetrics characterSize = CharacterMetrics.createCharacterMetrics(font);
      if (characterSize.getHeight() <= 6) {
         characterSize = new CharacterMetrics(characterSize.getWidth(), characterSize.getWidth() * 2, characterSize.getAscent());
      }

      int charAscent = characterSize.getAscent();
      int w = characterSize.getWidth() * cp.getWidth();
      int h = characterSize.getHeight() * cp.getHeight();
      BufferedImage image = new BufferedImage(w, h, 1);
      Graphics g = image.getGraphics();
      g.setColor(backgroundColor);
      g.fillRect(0, 0, w, h);
      g.setColor(foregroundColor);
      g.setFont(font);
      if (connectedLinesView) {
         ConnectedLinesViewRenderer.paintConnectedLinesView(g, cp, characterSize);
      } else {
         for (int y = 0; y < cp.getHeight(); y++) {
            g.drawString(cp.getLine(y), 0, charAscent + y * characterSize.getHeight());
         }
      }

      g.dispose();
      return image;
   }

   public static MemoryImageSource ascii2Image(char[][] ch, int scale, boolean negative) {
      return ascii2Image(new CharacterPlate(ch), scale, negative);
   }

   public static int[][] ascii2Pixels(char[][] ch, int resolution, boolean negative) {
      int width = ch[0].length;
      int height = ch.length;
      int[][] pixels = new int[width * resolution][height * resolution];
      switch (resolution) {
         case 1:
            for (int x = 0; x < width; x++) {
               for (int yxx = 0; yxx < height; yxx++) {
                  if (ch[yxx][x] >= ' ' && ch[yxx][x] <= '~') {
                     pixels[x][yxx] = TABLE_THUMB_1[ch[yxx][x] - ' '];
                  }
               }
            }
            break;
         case 2:
            for (int x = 0; x < width; x++) {
               for (int yx = 0; yx < height; yx++) {
                  int c = ch[yx][x] - ' ';
                  if (c >= 0 && c <= 94) {
                     pixels[2 * x][2 * yx] = TABLE_THUMB_4[c][0];
                     pixels[2 * x + 1][2 * yx] = TABLE_THUMB_4[c][1];
                     pixels[2 * x][2 * yx + 1] = TABLE_THUMB_4[c][2];
                     pixels[2 * x + 1][2 * yx + 1] = TABLE_THUMB_4[c][3];
                  }
               }
            }
            break;
         case 3:
            for (int x = 0; x < width; x++) {
               for (int y = 0; y < height; y++) {
                  char c = ch[y][x];
                  if (c >= ' ' && c <= '~') {
                     pixels[3 * x][3 * y] = TABLE_THUMB_9[c - ' '][0];
                     pixels[3 * x + 1][3 * y] = TABLE_THUMB_9[c - ' '][1];
                     pixels[3 * x + 2][3 * y] = TABLE_THUMB_9[c - ' '][2];
                     pixels[3 * x][3 * y + 1] = TABLE_THUMB_9[c - ' '][3];
                     pixels[3 * x + 1][3 * y + 1] = TABLE_THUMB_9[c - ' '][4];
                     pixels[3 * x + 2][3 * y + 1] = TABLE_THUMB_9[c - ' '][5];
                     pixels[3 * x][3 * y + 2] = TABLE_THUMB_9[c - ' '][6];
                     pixels[3 * x + 1][3 * y + 2] = TABLE_THUMB_9[c - ' '][7];
                     pixels[3 * x + 2][3 * y + 2] = TABLE_THUMB_9[c - ' '][8];
                  }
               }
            }
            break;
         default:
            throw new RuntimeException("No such resolution in ascii2Pixels: " + resolution);
      }

      if (negative) {
         for (int x = 0; x < width * resolution; x++) {
            for (int yxxx = 0; yxxx < height * resolution; yxxx++) {
               pixels[x][yxxx] = 255 - pixels[x][yxxx];
            }
         }
      }

      return pixels;
   }

   public static BufferedImage ascii2ImageQuick(CharacterPlate plate, int scale, Color bg, Color fg) {
      if (bg == null) {
         bg = Color.white;
      }

      if (fg == null) {
         fg = Color.black;
      }

      char[][] ch = plate.getContent();
      int width = plate.getWidth();
      int height = plate.getHeight();
      int[][] pixels = ascii2Pixels(ch, scale, false);
      int imageWidth = width * scale;
      int imageHeight = height * scale;
      int bgRed = bg.getRed();
      int bgGreen = bg.getGreen();
      int bgBlue = bg.getBlue();
      int fgRed = fg.getRed();
      int fgGreen = fg.getGreen();
      int fgBlue = fg.getBlue();
      double dRed = bgRed - fgRed;
      double dGreen = bgGreen - fgGreen;
      double dBlue = bgBlue - fgBlue;
      int[] newPixels = new int[imageWidth * imageHeight];

      for (int y = 0; y < imageHeight; y++) {
         for (int x = 0; x < imageWidth; x++) {
            double p = (double)pixels[x][y] / 255.0;
            int red = (int)(p * dRed + (double)fgRed);
            int green = (int)(p * dGreen + (double)fgGreen);
            int blue = (int)(p * dBlue + (double)fgBlue);
            newPixels[y * imageWidth + x] = 0xFF000000 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
         }
      }

      BufferedImage image = new BufferedImage(imageWidth, imageHeight, 2);
      image.getRaster().setDataElements(0, 0, imageWidth, imageHeight, newPixels);
      return image;
   }

   public static MemoryImageSource ascii2Image(CharacterPlate plate, int scale, boolean negative) {
      char[][] ch = plate.getContent();
      int width = plate.getWidth();
      int height = plate.getHeight();
      int[][] pixels = ascii2Pixels(ch, scale, negative);
      int imageWidth = width * scale;
      int imageHeight = height * scale;
      GGreyscaleImage result = new GGreyscaleImage(pixels);
      result = result.getXScaledInstance((int)((double)imageWidth * 0.67));
      result = result.sharpen(0.1);
      return result.getMemoryImageSource();
   }

   public static MemoryImageSource convertFile(String fileName, int maxWidth, int maxHeight, boolean negative) {
      return convertFile(fileName, maxWidth, maxHeight, new int[2], negative);
   }

   public static MemoryImageSource convertFile(String fileName, int maxWidth, int maxHeight, int[] fileDim, boolean negative) {
      return convertFile(new File(fileName), maxWidth, maxHeight, fileDim, negative);
   }

   public static MemoryImageSource convertFile(File file, int maxWidth, int maxHeight, int[] fileDim, boolean negative) {
      try {
         BufferedReader br = new BufferedReader(new FileReader(file));
         StringBuffer sb = new StringBuffer();
         String s = null;
         int w = 0;
         int h = 0;
         boolean reading = true;

         while ((s = br.readLine()) != null) {
            if (s.length() > w) {
               w = s.length();
            }

            if (reading) {
               sb.append(s);
               sb.append('\n');
            }

            if (reading && ++h > 80) {
               reading = false;
            }
         }

         br.close();
         fileDim[0] = w;
         fileDim[1] = h;
         return w != 0 && h != 0 ? convert(sb.substring(0, sb.length() - 1), maxWidth, maxHeight, null, null) : null;
      } catch (Exception var11) {
         System.err.println("Error loading Text-File! " + var11);
         return null;
      }
   }

   public static MemoryImageSource convertAnimationFile(File file, int maxWidth, int maxHeight, int[] fileDim) throws Exception {
      JaveAnimationFile jmov = new JaveAnimationFile(file);
      jmov.load(file.toURL());
      JaveAnimationFrame frame = null;
      int index = 0;

      do {
         frame = jmov.getFrame(index++);
      } while (frame.isEmpty() && index < jmov.getFrameCount());

      char[][] ch = frame.getContent();
      fileDim[0] = ch[0].length;
      fileDim[1] = ch.length;
      Color bg = jmov.getProperties().getBackgroundColor();
      Color fg = jmov.getProperties().getForegroundColor();
      return convert(ch, maxWidth, maxHeight, bg, fg);
   }

   public static MemoryImageSource convert(String text, int maxWidth, int maxHeight, Color bg, Color fg) {
      return convert(TextTools.toCharField(text), maxWidth, maxHeight, bg, fg);
   }

   public static MemoryImageSource convert(char[][] ch, int maxWidth, int maxHeight, Color bg, Color fg) {
      int height = ch.length;
      int width = ch[0].length;
      int scale = 2;
      if ((double)(width * 1) * 0.67 >= (double)maxWidth) {
         scale = 1;
      }

      int imageWidth = width * scale;
      int imageHeight = height * scale;
      int[][] pixels = ascii2Pixels(ch, scale, false);
      GGreyscaleImage result = new GGreyscaleImage(pixels);
      double scaleX = 0.67;
      int w = (int)((double)imageWidth * scaleX);
      double scaleY = 1.0;
      int h = (int)((double)imageHeight * scaleY);
      if (w > maxWidth) {
         scaleX = (double)maxWidth / (double)imageWidth;
         scaleY = 1.0 * scaleX / 0.67;
         h = (int)((double)imageHeight * scaleY);
      }

      if (h > maxHeight) {
         double s = (double)maxHeight / (double)h;
         scaleX *= s;
         scaleY *= s;
      }

      result = result.getScaledInstance(scaleX, scaleY);
      result = result.sharpen(0.1);
      return bg != null && fg != null ? result.getMemoryImageSource(bg, fg) : result.getMemoryImageSource();
   }
}
