package de.jave.image2ascii;

import de.jave.jave.configuration.ConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import net.dizzy.commons.core.io.IOUtilities;

public class AsciiGreyscaleTableReader {
   public static AsciiGreyscaleTable load(File file) throws ConfigurationException {
      char[] greyscales = new char[95];
      char[][] greyscales4 = new char[95][4];
      boolean[] defaultIgnores = new boolean[95];
      boolean[] defaultIgnores4 = new boolean[95];
      BufferedReader br = null;

      AsciiGreyscaleTable var32;
      try {
         br = new BufferedReader(new FileReader(file));
         String line = null;

         for (int i = 0; i < 95; i++) {
            line = br.readLine();
            if (line == null) {
               throw new Exception("Configuration file '" + file + "' is truncated.");
            }

            String value = null;
            if (line.endsWith("##")) {
               defaultIgnores[i] = true;
               defaultIgnores4[i] = true;
               value = line.substring(2, line.length() - 2);
            } else if (line.endsWith(" #")) {
               defaultIgnores[i] = false;
               defaultIgnores4[i] = true;
               value = line.substring(2, line.length() - 2);
            } else if (line.endsWith("#")) {
               defaultIgnores[i] = true;
               defaultIgnores4[i] = false;
               value = line.substring(2, line.length() - 1);
            } else {
               defaultIgnores[i] = false;
               defaultIgnores4[i] = false;
               value = line.substring(2);
            }

            try {
               int i1 = value.indexOf(32);
               int i2 = value.indexOf(32, i1 + 1);
               int i3 = value.indexOf(32, i2 + 1);
               int i4 = value.indexOf(32, i3 + 1);
               greyscales[i] = (char)Integer.parseInt(value.substring(0, i1));
               greyscales4[i][0] = (char)Integer.parseInt(value.substring(i1 + 1, i2));
               greyscales4[i][1] = (char)Integer.parseInt(value.substring(i2 + 1, i3));
               greyscales4[i][2] = (char)Integer.parseInt(value.substring(i3 + 1, i4));
               greyscales4[i][3] = (char)Integer.parseInt(value.substring(i4 + 1));
            } catch (Exception var21) {
               throw new Exception("Syntax error in configuration file '" + file + "': " + line);
            }
         }

         List<String> lines = new ArrayList<>();

         while ((line = br.readLine()) != null) {
            lines.add(line);
         }

         int size = lines.size();
         char[][] characters4 = new char[size][5];

         for (int i = 0; i < size; i++) {
            line = lines.get(i);

            try {
               int i1 = line.indexOf(32);
               int i2 = line.indexOf(32, i1 + 1);
               int i3 = line.indexOf(32, i2 + 1);
               int i4 = line.indexOf(32, i3 + 1);
               characters4[i][0] = (char)Integer.parseInt(line.substring(0, i1));
               characters4[i][1] = (char)Integer.parseInt(line.substring(i1 + 1, i2));
               characters4[i][2] = (char)Integer.parseInt(line.substring(i2 + 1, i3));
               characters4[i][3] = (char)Integer.parseInt(line.substring(i3 + 1, i4));
               characters4[i][4] = line.charAt(i4 + 1);
            } catch (Exception var20) {
               throw new ConfigurationException(file, "Syntax error: " + line, var20);
            }
         }

         var32 = new AsciiGreyscaleTable(defaultIgnores, defaultIgnores, greyscales, greyscales4, characters4);
      } catch (Exception var22) {
         throw new ConfigurationException(file, var22);
      } finally {
         IOUtilities.close(br);
      }

      return var32;
   }
}
