package de.jave.figlet.engine.controlfile;

import de.jave.core.io.zip.ZipUtilities;
import de.jave.figlet.engine.IFigConversionContext;
import de.jave.lib.io.IoUtilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import net.dizzy.commons.core.io.IOUtilities;

public class FigControlFile {
   public static final String SIGNATURE = "flc2a";
   public static final String BASIC_SIGNATURE = "flc";
   public static final String CONTROL_FILE_EXTENSION = ".flc";
   private String name;
   private FigControlRule[] rules;

   public FigControlFile(String name) {
      if (!name.endsWith(".flc")) {
         this.name = name + ".flc";
      } else {
         this.name = name;
      }
   }

   public int map(int character, IFigConversionContext context) {
      int result = -1;
      boolean stageFullFilled = false;

      for (int i = 0; i < this.rules.length; i++) {
         FigControlRule r = this.rules[i];
         if (r instanceof FigControlStageSeparator) {
            stageFullFilled = false;
         } else {
            int a = r.map(character, context);
            if (a != -1 && !stageFullFilled) {
               result = a;
               stageFullFilled = true;
            }
         }
      }

      return result == -1 ? character : result;
   }

   public boolean load(IFigConversionContext context) {
      List<FigControlRule> vRules = new ArrayList<>();
      InputStream inputStream = null;

      label78: {
         boolean reader;
         try {
            URL url = IoUtilities.toUrl(this.name);
            inputStream = url.openStream();
            BufferedReader readerx = new BufferedReader(new InputStreamReader(ZipUtilities.openPossiblyZipped(inputStream)));
            String line = readerx.readLine();

            while (true) {
               if (line == null) {
                  break label78;
               }

               if (line.length() != 0 && line.charAt(0) != '#' && !line.startsWith("flc")) {
                  switch (line.charAt(0)) {
                     case 'b':
                        context.addWarning(this.createWarning("Option 'b' not yet implemented - ignored."));
                        break;
                     case 'c':
                     case 'd':
                     case 'e':
                     case 'i':
                     case 'k':
                     case 'l':
                     case 'm':
                     case 'n':
                     case 'o':
                     case 'p':
                     case 'q':
                     case 'r':
                     case 's':
                     default:
                        context.addWarning(this.createWarning("'number number' not yet implemented - ignored."));
                        break;
                     case 'f':
                        vRules.add(new FigControlStageSeparator());
                        break;
                     case 'g':
                        context.addWarning(this.createWarning("Option 'g' not yet implemented - ignored."));
                        break;
                     case 'h':
                        context.addWarning(this.createWarning("Option 'h' not yet implemented - ignored."));
                        break;
                     case 'j':
                        context.addWarning(this.createWarning("Option 'j' not yet implemented - ignored."));
                        break;
                     case 't':
                        int i1 = 0;
                        if (line.startsWith("\\-")) {
                           i1 = 2;
                        }

                        if (line.indexOf(45, i1) == -1) {
                           vRules.add(new FigControlTranslation(line, context));
                        } else {
                           vRules.add(new FigControlRegionTranslation(line, context));
                        }
                        break;
                     case 'u':
                        context.addWarning(this.createWarning("Option 'u' not yet implemented - ignored."));
                  }
               }

               line = readerx.readLine();
            }
         } catch (IOException var11) {
            context.addWarning("Error loading FIGLet control file: " + var11);
            reader = false;
         } finally {
            IOUtilities.close(inputStream);
         }

         return reader;
      }

      this.rules = vRules.toArray(new FigControlRule[0]);
      return true;
   }

   private String createWarning(String message) {
      return "figlet control file " + this.name + ", " + message;
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("FIGLet control file '").append(this.getName()).append("' containing rules:\n");

      for (int i = 0; i < this.rules.length; i++) {
         sb.append(" ").append(i).append(" ").append(this.rules[i].toString()).append("\n");
      }

      return sb.toString();
   }

   public void dump() {
      System.out.println(this.toString());
   }

   public String getName() {
      return this.name;
   }
}
