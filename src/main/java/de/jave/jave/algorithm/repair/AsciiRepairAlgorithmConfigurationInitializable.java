package de.jave.jave.algorithm.repair;

import de.jave.jave.configuration.ConfigurationException;
import de.jave.jave.configuration.IJavaInitializationContext;
import de.jave.jave.configuration.IJaveInitializable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.disy.commons.core.io.IOUtilities;

public class AsciiRepairAlgorithmConfigurationInitializable implements IJaveInitializable<AsciiRepairAlgorithmConfiguration> {
   public AsciiRepairAlgorithmConfiguration initialize(IJavaInitializationContext context) throws ConfigurationException {
      File configurationFile = context.getConfigurationFile("./config/repair.txt");
      List<AsciiRepairRule> rulesList = new ArrayList<>();
      BufferedReader br = null;

      int identical;
      int identicalLeft;
      int identicalRight;
      try {
         br = new BufferedReader(new FileReader(configurationFile));
         int blockCount = 0;
         int lineCount = 0;
         String line1 = br.readLine();
         lineCount++;

         while (line1 != null && (line1.length() < 1 || line1.charAt(0) == '#')) {
            line1 = br.readLine();
            lineCount++;
         }

         identical = Integer.parseInt(line1);
         line1 = br.readLine();
         lineCount++;

         while (line1 != null && (line1.length() < 1 || line1.charAt(0) == '#')) {
            line1 = br.readLine();
            lineCount++;
         }

         identicalLeft = Integer.parseInt(line1);
         line1 = br.readLine();
         lineCount++;

         while (line1 != null && (line1.length() < 1 || line1.charAt(0) == '#')) {
            line1 = br.readLine();
            lineCount++;
         }

         identicalRight = Integer.parseInt(line1);

         while (line1 != null) {
            line1 = br.readLine();
            lineCount++;

            while (line1 != null && (line1.length() < 1 || line1.charAt(0) == '#')) {
               line1 = br.readLine();
               lineCount++;
            }

            if (line1 == null) {
               break;
            }

            String line2 = br.readLine();
            lineCount++;

            while (line2 != null && (line2.length() < 1 || line2.charAt(0) == '#')) {
               line2 = br.readLine();
               lineCount++;
            }

            if (line2 == null) {
               break;
            }

            String line3 = br.readLine();
            lineCount++;

            while (line3 != null && (line3.length() < 1 || line3.charAt(0) == '#')) {
               line3 = br.readLine();
               lineCount++;
            }

            if (line3 == null) {
               break;
            }

            AsciiRepairRule r = AsciiRepairRule.getFrom(line1, line2, line3);
            if (r != null) {
               rulesList.add(r);
            }
         }
      } catch (IOException var17) {
         throw new ConfigurationException(configurationFile, var17);
      } finally {
         IOUtilities.close(br);
      }

      AsciiRepairRule[] var21 = rulesList.toArray(new AsciiRepairRule[0]);
      return new AsciiRepairAlgorithmConfiguration(var21, identical, identicalLeft, identicalRight);
   }
}
