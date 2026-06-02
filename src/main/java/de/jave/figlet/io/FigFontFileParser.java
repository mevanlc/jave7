package de.jave.figlet.io;

import de.jave.core.io.zip.ZipUtilities;
import de.jave.figlet.engine.layout.PrintDirection;
import de.jave.figlet.engine.primitives.FigCharacter;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.engine.primitives.FigFontOptions;
import de.jave.figlet.engine.primitives.FigLayout;
import de.jave.figlet.engine.primitives.FigLayoutEncoder;
import de.jave.figlet.file.FigFileName;
import de.jave.figlet.file.IFigFileResource;
import de.jave.figlet.util.FigException;
import de.jave.figlet.util.FigFileFormatException;
import de.jave.figlet.util.FigUtilities;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.StringTokenizer;
import net.dizzy.commons.core.io.IOUtilities;

public class FigFontFileParser {
   private static final String SIGNATURE = "flf2a";

   public FigFont load(FigFileName fileDescription, IFigFileResource fileResource) throws FigException {
      Reader reader = null;

      FigFont var5;
      try {
         InputStream inputStream = fileResource.openFigFileInputStream(fileDescription);
         if (inputStream == null) {
            throw new FigException(
               "Error loading FIGlet font '"
                  + fileDescription.getName()
                  + "' - the resource does not seem to exist ("
                  + fileResource.getRourceBaseDescription()
                  + ").",
               6
            );
         }

         reader = new InputStreamReader(ZipUtilities.openPossiblyZipped(inputStream));
         var5 = this.readFont(fileDescription, reader);
      } catch (FileNotFoundException var10) {
         throw new FigException(
            "Error loading FIGlet font '" + fileDescription.getName() + "' - the resource does not exist (" + fileResource.getRourceBaseDescription() + ").",
            var10,
            6
         );
      } catch (IOException var11) {
         throw new FigException("Error loading FIGlet font '" + fileDescription.getName() + "'", var11, 6);
      } finally {
         IOUtilities.close(reader);
      }

      return var5;
   }

   public FigFont readFont(FigFileName fileDescription, Reader reader) throws FigFileFormatException, IOException {
      return this.readFont(fileDescription, new LineNumberReader(reader));
   }

   private FigFont readFont(FigFileName fileDescription, LineNumberReader reader) throws FigFileFormatException, IOException {
      return this.readFigFont(reader, fileDescription.getPrintName());
   }

   private FigFont readFigFont(LineNumberReader reader, String fontName) throws FigFileFormatException, IOException {
      String line = reader.readLine();
      if (line == null) {
         throw this.createFigFileFormatException("Unexpected end of FIGlet font file in header", reader.getLineNumber());
      } else {
         FigFontOptions options = this.readBasicOptions(line);
         this.readCommentLines(reader, options);
         FigFont font = new FigFont(fontName, options);

         for (int j = 0; j < options.getCodetagCount() + 102; j++) {
            FigCharacter character = this.loadCharacter(reader, j, options);
            if (character == null) {
               break;
            }

            font.addCharacter(character);
            int width = character.getWidth();
            if (width > font.getOptions().getActualMaxLineWidth()) {
               font.getOptions().setActualMaxLineWidth(width);
            }
         }

         return font;
      }
   }

   private void readCommentLines(LineNumberReader reader, FigFontOptions options) throws IOException, FigFileFormatException {
      if (options.getCommentLineCount() != 0) {
         StringBuffer sb = new StringBuffer();

         for (int i = 0; i < options.getCommentLineCount(); i++) {
            if (i > 0) {
               sb.append("\n");
            }

            String line = reader.readLine();
            if (line == null) {
               throw this.createFigFileFormatException("Unexpected end of FIGlet font file in comment line", reader.getLineNumber());
            }

            sb.append(line);
         }

         options.setComments(sb.toString());
      }
   }

   private FigCharacter loadCharacter(LineNumberReader reader, int count, FigFontOptions options) throws IOException, FigFileFormatException {
      int characterCode;
      if (count <= 94) {
         characterCode = count + 32;
      } else if (count == 95) {
         characterCode = 196;
      } else if (count == 96) {
         characterCode = 214;
      } else if (count == 97) {
         characterCode = 220;
      } else if (count == 98) {
         characterCode = 228;
      } else if (count == 99) {
         characterCode = 246;
      } else if (count == 100) {
         characterCode = 252;
      } else if (count == 101) {
         characterCode = 223;
      } else {
         String info = reader.readLine();
         if (info == null || info.isEmpty()) {
            return null;
         }

         try {
            characterCode = this.readCharacterCode(info);
         } catch (NumberFormatException var10) {
            throw this.createFigFileFormatException("Error reading character code. Expected numeric value, was '" + info + "'", reader.getLineNumber());
         }
      }

      String[] lines = new String[options.getHeight()];

      for (int i = 0; i < lines.length; i++) {
         String line = reader.readLine();
         if (line == null || line.length() == 0) {
            throw this.createFigFileFormatException("Unexpected end of FIGlet font file in character code='" + characterCode + "'", reader.getLineNumber());
         }

         char eol = line.charAt(line.length() - 1);
         int cutOff = 1;
         if (line.length() > 1 && line.charAt(line.length() - 2) == eol) {
            cutOff++;
         }

         lines[i] = line.substring(0, line.length() - cutOff).replace(options.getHardblank(), '\u007f');
      }

      return new FigCharacter(characterCode, lines);
   }

   private FigFileFormatException createFigFileFormatException(String message, int lineNumber) {
      return new FigFileFormatException(message + " at input line " + lineNumber);
   }

   private int readCharacterCode(String info) {
      boolean negative = false;
      if (info.charAt(0) == '-') {
         negative = true;
         info = info.substring(1);
      }

      int characterCode;
      if (info.charAt(0) == '0') {
         if (info.length() < 2) {
            characterCode = 0;
         } else if (info.charAt(1) != 'x' && info.charAt(1) != 'X') {
            int i = info.indexOf(" ");
            if (i == -1) {
               i = info.length();
            }

            characterCode = Integer.parseInt(info.substring(1, i), 8);
         } else {
            int i = info.indexOf(" ");
            if (i == -1) {
               i = info.length();
            }

            characterCode = Integer.parseInt(info.substring(2, i), 16);
         }
      } else {
         int i = info.indexOf(" ");
         if (i == -1) {
            i = info.length();
         }

         characterCode = Integer.parseInt(info.substring(0, i));
      }

      if (negative) {
         characterCode = -characterCode;
      }

      return characterCode;
   }

   private FigFontOptions readBasicOptions(String header) throws FigFileFormatException {
      if (header.length() < 15) {
         throw new FigFileFormatException("Unable to read header '" + header + "'");
      } else {
         StringTokenizer st = new StringTokenizer(header);
         if (!st.hasMoreTokens()) {
            throw new FigFileFormatException("Unable to read header '" + header + "'");
         } else {
            String signature = st.nextToken();
            if (signature.startsWith("flf2a") && signature.length() == 6) {
               FigFontOptions options = new FigFontOptions();
               options.setHardblank(signature.charAt(5));
               options.setHeight(this.readNextInt(st, "height", header));
               options.setBaseline(this.readNextInt(st, "baseline", header));
               options.setMaxLength(this.readNextInt(st, "maxlength", header));
               int oldLayout = this.readNextInt(st, "old_layout", header);
               FigLayout layout = new FigLayoutEncoder().createFromOldLayoutValue(oldLayout);
               options.setLayout(layout);
               options.setCommentLineCount(this.readNextInt(st, "comment_lines", header));
               options.setCodetagCount(102);
               if (!st.hasMoreTokens()) {
                  return options;
               } else {
                  int printDirectionValue = this.readNextInt(st, "print_direction", header);
                  PrintDirection printDirection = new FigLayoutEncoder().getPrintDirection(printDirectionValue);
                  if (printDirection == null) {
                     throw new FigFileFormatException("Illegal value for printDirection: " + printDirectionValue);
                  } else {
                     layout.setPrintDirection(printDirection);
                     if (!st.hasMoreTokens()) {
                        return options;
                     } else {
                        int fullLayout = this.readNextInt(st, "full_layout", header);
                        options.setLayout(new FigLayoutEncoder().createFromCombinedLayoutValues(oldLayout, fullLayout));
                        if (!st.hasMoreTokens()) {
                           return options;
                        } else {
                           options.setCodetagCount(this.readNextInt(st, "codetag_count", header));
                           return options;
                        }
                     }
                  }
               }
            } else {
               throw new FigFileFormatException("Illegal signature, was '" + signature + "' expected '" + "flf2a" + "' followed by a character.");
            }
         }
      }
   }

   private void checkHasNextAttribute(StringTokenizer tokenizer, String attributeName, String header) throws FigFileFormatException {
      if (!tokenizer.hasMoreTokens()) {
         throw new FigFileFormatException("Unable to read header '" + header + "' - " + attributeName + " option missing.");
      }
   }

   private int readNextInt(StringTokenizer tokenizer, String attributeName, String header) throws FigFileFormatException {
      this.checkHasNextAttribute(tokenizer, attributeName, header);
      String attribute = tokenizer.nextToken();

      try {
         return FigUtilities.stringToInt(attribute);
      } catch (NumberFormatException var7) {
         throw new FigFileFormatException("Unable to read " + attributeName + " option from header ", var7);
      }
   }

   public FigFont readFont(FigFileName description, File file) throws FigFileFormatException, IOException {
      FileReader reader = null;

      FigFont var4;
      try {
         reader = new FileReader(file);
         var4 = this.readFont(description, reader);
      } finally {
         IOUtilities.close(reader);
      }

      return var4;
   }
}
