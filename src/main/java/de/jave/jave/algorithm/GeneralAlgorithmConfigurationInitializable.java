package de.jave.jave.algorithm;

import de.jave.jave.configuration.ConfigurationException;
import de.jave.jave.configuration.IJavaInitializationContext;
import de.jave.jave.configuration.IJaveInitializable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import net.dizzy.commons.core.io.IOUtilities;

public class GeneralAlgorithmConfigurationInitializable implements IJaveInitializable<GeneralAlgorithmConfiguration> {
   public GeneralAlgorithmConfiguration initialize(IJavaInitializationContext context) throws ConfigurationException {
      File configurationFile = context.getConfigurationFile("./config/general.txt");
      String mirror = null;
      String flip = null;
      String rotate180 = null;
      String rotate90R = null;
      String rotate90L = null;
      BufferedReader br = null;

      try {
         br = new BufferedReader(new FileReader(configurationFile));
         String line = null;

         while ((line = br.readLine()) != null) {
            if (line.length() > 0 && line.charAt(0) != '#') {
               if (line.trim().startsWith("MIRROR=")) {
                  int i1 = line.indexOf(61);
                  mirror = line.substring(i1 + 1);
               } else if (line.trim().startsWith("FLIP=")) {
                  int i1 = line.indexOf(61);
                  flip = line.substring(i1 + 1);
               } else if (line.trim().startsWith("ROTATE180=")) {
                  int i1 = line.indexOf(61);
                  rotate180 = line.substring(i1 + 1);
               } else if (line.trim().startsWith("ROTATE90R=")) {
                  int i1 = line.indexOf(61);
                  rotate90R = line.substring(i1 + 1);
               } else if (line.trim().startsWith("ROTATE90L=")) {
                  int i1 = line.indexOf(61);
                  rotate90L = line.substring(i1 + 1);
               }
            }
         }
      } catch (IOException var14) {
         throw new ConfigurationException(configurationFile, var14);
      } finally {
         IOUtilities.close(br);
      }

      if (isNotWellDefined(mirror)) {
         throw new ConfigurationException(configurationFile, "MIRROR is not defined or erratic.");
      } else if (isNotWellDefined(flip)) {
         throw new ConfigurationException(configurationFile, "FLIP is not defined or erratic.");
      } else if (isNotWellDefined(rotate180)) {
         throw new ConfigurationException(configurationFile, "ROTATE180 is not defined or erratic.");
      } else if (isNotWellDefined(rotate90R)) {
         throw new ConfigurationException(configurationFile, "ROTATE90R is not defined or erratic.");
      } else if (isNotWellDefined(rotate90L)) {
         throw new ConfigurationException(configurationFile, "ROTATE90L is not defined or erratic.");
      } else {
         return new GeneralAlgorithmConfiguration(mirror, flip, rotate180, rotate90R, rotate90L);
      }
   }

   private static boolean isNotWellDefined(String code) {
      return code == null || code.length() != 95;
   }
}
