package de.jave.figlet.engine;

import de.jave.figlet.engine.controlfile.FigControlFile;
import de.jave.figlet.engine.controlfile.FigControlFileList;
import de.jave.figlet.engine.layout.HorizontalAlignment;
import de.jave.figlet.engine.layout.VerticalAlignment;
import de.jave.figlet.engine.layouter.DefaultFigLayouter;
import de.jave.figlet.engine.layouter.IFigLayouter;
import de.jave.figlet.engine.output.AsciiOutputConverter;
import de.jave.figlet.engine.output.OutputFormat;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.engine.primitives.FigFragment;
import de.jave.figlet.engine.primitives.FigLayout;
import de.jave.figlet.engine.processing.FigletJobFactory;
import de.jave.figlet.engine.processing.IFigletJob;
import de.jave.figlet.file.FigFileLibrary;
import de.jave.figlet.file.FigFileName;
import de.jave.figlet.file.IFigFileLibrary;
import de.jave.figlet.file.IFigFileResource;
import de.jave.figlet.io.FigFontFileParser;
import de.jave.figlet.util.FigException;
import de.jave.lib.job.IWarningCollector;
import de.jave.lib.job.NullWarningCollector;
import de.jave.lib.net.HtmlUtilities;
import de.jave.text.QuickString;
import de.jave.text.TextTools;
import java.io.File;

public class FigDriver implements IFigDriver {
   private final FigFileLibrary fileLibrary;
   public static final String PROGRAM_NAME = "FigletFIGML 0.1";
   public static final int LINE_LENGTH_DEFAULT = 80;
   private final IFigFontProvider cache;
   private FigFont font;
   private FigControlFileList controlFiles;
   private VerticalAlignment vAlign = VerticalAlignment.BOTTOM;
   private HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
   private IWarningCollector warner = new NullWarningCollector();

   public FigDriver(final IFigFileResource resource) throws FigException {
      this.fileLibrary = FigFileLibrary.loadFileLibrary(resource);
      IFigFontLoader fontLoader = new IFigFontLoader() {
         @Override
         public FigFont loadFont(String fontName) throws FigException {
            return new FigFontFileParser().load(new FigFileName(fontName), resource);
         }

         @Override
         public long getLastModificationTime(String fontName) throws FigException {
            return resource.getLastModified(new FigFileName(fontName));
         }
      };
      this.cache = new FigFontCache(fontLoader);
   }

   public IWarningCollector getWarningCollector() {
      return this.warner;
   }

   @Deprecated
   public void setFont(String name) throws FigException {
      FigFont f = this.getFont(name);
      if (f != null) {
         this.font = f;
      } else {
         this.font = this.cache.getFont(this.fileLibrary.getDefaultFontName());
      }
   }

   public void forceFontReload(String name, File file) throws FigException {
      FigFont f;
      try {
         f = new FigFontFileParser().readFont(new FigFileName(file.getAbsolutePath()), file);
      } catch (Exception var5) {
         throw new FigException("Unable to load font from '" + file.getAbsolutePath() + "'", var5);
      }

      if (f != null) {
         this.font = f;
      } else {
         this.font = this.cache.getFont(this.fileLibrary.getDefaultFontName());
      }
   }

   @Override
   public FigFont getFont(String name) throws FigException {
      return this.cache.getFont(name);
   }

   public int getFontHeight(String name) throws FigException {
      FigFont f = this.getFont(name);
      return f != null ? f.getHeight() : 1;
   }

   public int getFontUnderLength(String name) throws FigException {
      FigFont f = this.getFont(name);
      return f != null ? f.getUnderLength() : 0;
   }

   private void addControlFile(String name) {
      if (this.controlFiles == null) {
         this.controlFiles = new FigControlFileList();
      }

      FigControlFile fc = new FigControlFile(name);
      IFigConversionContext context = new DefaultConversionContext(this.warner);
      boolean succeed = fc.load(context);
      if (!succeed) {
         context.addWarning("Unable to load control file " + name + " - ignored.");
      } else {
         this.controlFiles.add(fc);
      }
   }

   private void removeControlFile(String name) {
      boolean succeed = this.controlFiles.remove(name);
      if (!succeed) {
         this.warning("Trying to close control file " + name + " that has not been loaded - ignored.");
      }
   }

   private void removeAllControlFiles() {
      this.controlFiles = null;
   }

   private void setVAlignment(String a) {
      a = a.toLowerCase();
      if (a.equals("bottom")) {
         this.vAlign = VerticalAlignment.BOTTOM;
      } else if (a.equals("top")) {
         this.vAlign = VerticalAlignment.TOP;
      } else if (a.equals("center")) {
         this.vAlign = VerticalAlignment.CENTER;
      } else {
         System.err.println("Unbekannte Option für vertikales Alignment: " + a);
         this.vAlign = VerticalAlignment.BOTTOM;
      }
   }

   private void setHAlignment(String a) {
      a = a.toLowerCase();
      if (a.equals("left")) {
         this.horizontalAlignment = HorizontalAlignment.LEFT;
      } else if (a.equals("right")) {
         this.horizontalAlignment = HorizontalAlignment.RIGHT;
      } else if (a.equals("center")) {
         this.horizontalAlignment = HorizontalAlignment.CENTER;
      } else {
         System.err.println("Unbekannte Option für horizontales Alignment: " + a);
         this.horizontalAlignment = HorizontalAlignment.LEFT;
      }
   }

   private FigFragment figletizeRaw(String word, int maxLineLength, IFigLayouter layouter, IFigConversionContext context) {
      word = HtmlUtilities.decodeEntities(word);
      if (this.font == null) {
         System.err.println("FONT==NULL!!!!!");
      }

      FigFragment result = null;

      for (int i = 0; i < word.length(); i++) {
         char ch = word.charAt(i);
         int buchstabe = ch;
         if (this.controlFiles != null) {
            buchstabe = this.controlFiles.map(ch, context);
         }

         if (result == null) {
            result = layouter.createFragment(this.font, this.font.getLayout(), buchstabe);
         } else {
            result = layouter.appendHorizontal(result, layouter.createFragment(this.font, this.font.getLayout(), buchstabe));
         }
      }

      result.setVerticalAlignment(this.vAlign);
      result.setHorizontalAlignment(this.horizontalAlignment);
      if (result.getWidth() > maxLineLength) {
         this.warning("The word \"" + word + "\"exceeds the maximum length of a single line.");
      }

      return result;
   }

   public void warning(String s) {
      if (this.warner != null) {
         this.warner.addWarning(s);
      }
   }

   private String FIGMLParse(String figSource, String options, IFigConversionContext context) throws FigException {
      int format = 0;

      for (int i = 0; i < OutputFormat.FORMAT_STR.length; i++) {
         int i1 = options.indexOf(OutputFormat.FORMAT_STR[i]);
         if (i1 != -1) {
            format = i;
            options = options.substring(i1 + OutputFormat.FORMAT_STR[i].length()).trim();
            break;
         }
      }

      int maxLineLength = 80;

      try {
         maxLineLength = Integer.parseInt(options);
      } catch (NumberFormatException var14) {
         this.warning("Illegal option for length of line: '" + options + "'");
      }

      IFigLayouter layouter = new DefaultFigLayouter(maxLineLength);

      for (int i1 = figSource.indexOf("\n"); i1 != -1; i1 = figSource.indexOf("\n", i1)) {
         if (i1 == figSource.length()) {
            figSource = figSource.substring(0, figSource.length() - 1);
         } else if (i1 == 0) {
            figSource = figSource.substring(1);
         } else if (figSource.charAt(i1 - 1) == '>') {
            figSource = figSource.substring(0, i1) + figSource.substring(i1 + 1);
         } else {
            figSource = figSource.substring(0, i1) + " " + figSource.substring(i1 + 1);
         }
      }

      figSource = figSource.replace('\t', ' ');
      figSource = figSource.replace('\f', ' ');
      figSource = figSource.replace('\r', ' ');

      for (int var19 = figSource.indexOf("  "); var19 != -1; var19 = figSource.indexOf("  ")) {
         figSource = figSource.substring(0, var19) + figSource.substring(var19 + 1);
      }

      for (int var20 = figSource.indexOf(" <br>"); var20 != -1; var20 = figSource.indexOf(" <br>")) {
         figSource = figSource.substring(0, var20) + figSource.substring(var20 + 1);
      }

      FigFragment resultFragment = null;
      FigFragment lineFragment = null;
      FigmlTokenizer tim = new FigmlTokenizer(figSource);

      for (FigmlToken token = tim.nextToken(); token != null; token = tim.nextToken()) {
         if (token.getType() == 6) {
            this.setFont(token.getText());
         } else if (token.getType() == 7) {
            this.addControlFile(token.getText());
         } else if (token.getType() == 8) {
            this.removeControlFile(token.getText());
         } else if (token.getType() == 9) {
            this.removeAllControlFiles();
         } else if (token.getType() == 4) {
            this.setVAlignment(token.getText());
         } else if (token.getType() == 5) {
            this.setHAlignment(token.getText());
         } else if (token.getType() == 1) {
            if (lineFragment == null) {
               lineFragment = new FigFragment(this.font.getFIGCharacter(32), this.font.getLayout(), this.font.getUnderLength(), maxLineLength);
            }

            if (resultFragment == null) {
               resultFragment = (FigFragment)lineFragment.clone();
            } else {
               resultFragment = layouter.appendVertical(resultFragment, lineFragment);
            }

            lineFragment = null;
         } else if (token.getType() != 11) {
            if (token.getType() == 0) {
               FigFragment a = this.figletizeRaw(token.getText(), maxLineLength, layouter, context);
               a.setVerticalAlignment(this.vAlign);
               a.setHorizontalAlignment(this.horizontalAlignment);
               if (lineFragment == null) {
                  lineFragment = a;
               } else {
                  FigFragment test = (FigFragment)lineFragment.clone();
                  test = layouter.appendHorizontal(test, a);
                  if (test.getWidth() > maxLineLength) {
                     if (resultFragment == null) {
                        resultFragment = lineFragment;
                     } else {
                        resultFragment = layouter.appendVertical(resultFragment, lineFragment);
                     }

                     lineFragment = a;
                  } else {
                     lineFragment = test;
                     test.setVerticalAlignment(this.vAlign);
                     test.setHorizontalAlignment(this.horizontalAlignment);
                  }
               }
            } else {
               System.out.println("Token noch unbekannt - ignoriert");
            }
         } else {
            if (lineFragment == null) {
               lineFragment = new FigFragment(this.font.getFIGCharacter(32), this.font.getLayout(), this.font.getUnderLength(), maxLineLength);
            }

            if (resultFragment == null) {
               resultFragment = (FigFragment)lineFragment.clone();
            } else {
               resultFragment = layouter.appendVertical(resultFragment, lineFragment);
            }

            FigFragment a = this.figletizeRaw(token.getText(), maxLineLength, layouter, context);
            if (a.getWidth() == 1) {
               a.ensureUnsmushability();
            }

            a.setVerticalAlignment(VerticalAlignment.CENTER);
            a.setHorizontalAlignment(HorizontalAlignment.CENTER);
            lineFragment = a;
            a = this.figletizeRaw(token.getText(), maxLineLength, layouter, context);
            if (a.getWidth() == 1) {
               a.ensureUnsmushability();
            }

            while (a.getWidth() + lineFragment.getWidth() < maxLineLength) {
               lineFragment = layouter.appendHorizontal(lineFragment, a);
            }

            if (resultFragment == null) {
               resultFragment = lineFragment;
            } else {
               resultFragment = layouter.appendVertical(resultFragment, lineFragment);
            }

            lineFragment = null;
         }
      }

      if (lineFragment == null && resultFragment == null) {
         resultFragment = new FigFragment(this.font.getFIGCharacter(32), this.font.getLayout(), this.font.getUnderLength(), maxLineLength);
      } else if (resultFragment == null && lineFragment != null) {
         resultFragment = lineFragment;
      } else if (resultFragment != null && lineFragment != null) {
         resultFragment = layouter.appendVertical(resultFragment, lineFragment);
      }

      return AsciiOutputConverter.getAs(resultFragment, format);
   }

   private void reset() {
      try {
         this.setFont(this.getFileLibrary().getDefaultFontName());
      } catch (FigException var2) {
         var2.printStackTrace();
      }

      this.removeAllControlFiles();
      this.vAlign = VerticalAlignment.BOTTOM;
      this.horizontalAlignment = HorizontalAlignment.LEFT;
   }

   @Override
   public String figml(String input) throws FigException {
      IFigConversionContext context = new DefaultConversionContext(this.warner);
      if (input.startsWith("<raw ")) {
         int i1 = input.indexOf(62);
         if (i1 < 5) {
            return "";
         } else {
            String fontName = input.substring(5, i1);
            this.reset();
            this.setFont(fontName);
            String source = input.substring(i1 + 1);
            if (source.length() == 0) {
               return "";
            } else {
               FigFragment fragment = null;

               for (int i = 0; i < source.length(); i++) {
                  char ch = source.charAt(i);
                  DefaultFigLayouter layouter = new DefaultFigLayouter(100);
                  if (fragment != null) {
                     fragment = layouter.appendHorizontal(fragment, layouter.createFragment(this.font, this.font.getLayout(), ch));
                  } else {
                     fragment = layouter.createFragment(this.font, this.font.getLayout(), ch);
                  }
               }

               return AsciiOutputConverter.getAs(fragment, -1);
            }
         }
      } else {
         StringBuffer output = new StringBuffer();

         for (int i1 = input.indexOf("<figlet"); i1 != -1; i1 = input.indexOf("<figlet")) {
            output.append(input, 0, i1);
            int i2 = input.indexOf(">", i1 + 5);
            if (i2 == -1) {
            }

            String options = input.substring(i1 + 7, i2).trim().toLowerCase();
            int i3 = input.indexOf("</fig");
            if (i3 == -1) {
               i3 = input.length() - 5;
            }

            int i4 = input.indexOf(">", i3);
            if (i4 == -1) {
               i4 = i3;
            }

            String figSource = input.substring(i2 + 1, i3);
            this.reset();
            String figResult = this.FIGMLParse(figSource, options, context);
            output.append(figResult);
            input = input.substring(i4 + 1);
         }

         output.append(input);
         return output.toString();
      }
   }

   @Override
   public IFigFileLibrary getFileLibrary() {
      return this.fileLibrary;
   }

   @Override
   public String figletize(String text, String fontName) throws FigException {
      try {
         return this.figletize(FigletJobFactory.createJob(text, fontName));
      } catch (RuntimeException var4) {
         throw new RuntimeException("Error figletizing with font '" + fontName + "'", var4);
      }
   }

   private FigFragment convertLine(String text, FigLayout layout, IFigLayouter layouter, FigFont font, IFigConversionContext context) {
      if (text.length() == 0) {
         return new FigFragment(
            new QuickString[0], new FigLayout[0], new FigLayout[0], 0, 0, new FigLayout[0], new FigLayout[0], 0, -1, layout.getPrintDirection()
         );
      } else {
         FigFragment result = null;

         for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            int characterCode = ch;
            if (this.controlFiles != null) {
               characterCode = this.controlFiles.map(ch, context);
            }

            if (result == null) {
               result = layouter.createFragment(font, layout, characterCode);
            } else {
               result = layouter.appendHorizontal(result, layouter.createFragment(font, layout, characterCode));
            }
         }

         return result;
      }
   }

   public void setWarningCollector(IWarningCollector warner) {
      this.warner = warner;
   }

   @Override
   public String figletize(IFigletJob job) throws FigException {
      FigFont font = job.getFont(this);
      FigLayout layout = job.getLayout();
      if (layout == null) {
         layout = font.getLayout();
      }

      IFigLayouter layouter = new DefaultFigLayouter(-1);
      IFigConversionContext context = new DefaultConversionContext(this.warner);
      String[] lines = TextTools.toStringArray(job.getText());
      FigFragment result = null;

      for (int i = 0; i < lines.length; i++) {
         FigFragment convertedLine = this.convertLine(lines[i], layout, layouter, font, context);
         convertedLine.setHorizontalAlignment(job.getHorizontalAlignment());
         if (result == null) {
            result = convertedLine;
         } else {
            result = layouter.appendVertical(result, convertedLine);
         }
      }

      return result == null ? "" : AsciiOutputConverter.getAs(result, 0);
   }
}
