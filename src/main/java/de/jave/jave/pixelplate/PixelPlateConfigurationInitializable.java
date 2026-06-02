package de.jave.jave.pixelplate;

import de.jave.jave.configuration.ConfigurationException;
import de.jave.jave.configuration.IJavaInitializationContext;
import de.jave.jave.configuration.IJaveInitializable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import net.dizzy.commons.core.io.IOUtilities;

public class PixelPlateConfigurationInitializable implements IJaveInitializable<PixelPlateConfiguration> {
   public PixelPlateConfiguration initialize(IJavaInitializationContext context) throws ConfigurationException {
      File configurationFile0 = context.getConfigurationFile("./config/convert.txt");
      char[] rules = loadRules(configurationFile0);
      File configurationFile1 = context.getConfigurationFile("./config/convert_thick.txt");
      char[] rulesThick = loadRules(configurationFile1);
      return new PixelPlateConfiguration(rules, rulesThick);
   }

   private static char[] loadRules(File file) throws ConfigurationException {
      char[] rules = new char[4096];

      for (int i = 0; i < rules.length; i++) {
         rules[i] = 0;
      }

      BufferedReader br = null;

      try {
         br = new BufferedReader(new FileReader(file));
         String[] lines = new String[4];
         int blockCount = 0;
         int lineCount = 0;

         while (lines != null) {
            String line = br.readLine();
            lineCount++;

            while (line != null && (line.length() < 3 || line.charAt(0) == '#')) {
               line = br.readLine();
               lineCount++;
            }

            if (line == null) {
               break;
            }

            lines[0] = line;

            for (int i = 1; i < lines.length; i++) {
               lines[i] = br.readLine();
               lineCount++;
               if (lines[i] == null) {
                  lines = null;
                  break;
               }
            }

            if (lines != null) {
               char[] pattern = new char[12];
               char ch = lines[3].charAt(8);
               int index = 0;

               for (int x = 0; x < 3; x++) {
                  for (int y = 0; y < 4; y++) {
                     if (lines[y].length() <= x) {
                        pattern[x * 4 + y] = '.';
                     } else {
                        pattern[x * 4 + y] = lines[y].charAt(x);
                     }
                  }
               }

               addRule(lineCount - 4, blockCount++, pattern, ch, rules);
            }
         }
      } catch (IOException var15) {
         throw new ConfigurationException(file, var15);
      } finally {
         IOUtilities.close(br);
      }

      int var18 = 0;
      int var19 = 0;

      for (int var20 = 0; var20 < rules.length; var20++) {
         if (rules[var20] != 0) {
            var18++;
         } else {
            var19++;
         }
      }

      return rules;
   }

   private static void addRule(int lineCount, int blockCount, char[] pattern, char ch, char[] rules) {
      for (int i = 0; i < 12; i++) {
         if (pattern[i] == '?') {
            char[] pattern2 = new char[12];
            System.arraycopy(pattern, 0, pattern2, 0, 12);
            pattern2[i] = 'X';
            pattern[i] = '.';
            addRule(lineCount, blockCount, pattern, ch, rules);
            addRule(lineCount, blockCount, pattern2, ch, rules);
            return;
         }
      }

      int index = 0;

      for (int ix = 0; ix < 12; ix++) {
         if (pattern[ix] == 'X') {
            index |= 1 << ix;
         }
      }

      if (rules[index] == 0 || rules[index] == ch) {
         rules[index] = ch;
      }
   }
}
