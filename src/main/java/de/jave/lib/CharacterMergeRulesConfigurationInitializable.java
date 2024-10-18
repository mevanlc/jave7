package de.jave.lib;

import de.jave.jave.configuration.ConfigurationException;
import de.jave.jave.configuration.IJavaInitializationContext;
import de.jave.jave.configuration.IJaveInitializable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import net.disy.commons.core.io.IOUtilities;

public class CharacterMergeRulesConfigurationInitializable implements IJaveInitializable<CharacterMergeRulesConfiguration> {
   public CharacterMergeRulesConfiguration initialize(IJavaInitializationContext context) throws ConfigurationException {
      File configurationFile = context.getConfigurationFile("./config/mix.txt");
      StringBuffer ra = new StringBuffer();
      StringBuffer rb = new StringBuffer();
      StringBuffer rc = new StringBuffer();
      BufferedReader br = null;

      try {
         br = new BufferedReader(new FileReader(configurationFile));
         String line = null;

         while ((line = br.readLine()) != null) {
            if (line.length() == 3) {
               char chA = line.charAt(0);
               char chB = line.charAt(1);
               char chC = line.charAt(2);
               ra.append(chA);
               rb.append(chB);
               rc.append(chC);
            }
         }
      } catch (IOException var14) {
         throw new ConfigurationException(configurationFile, var14);
      } finally {
         IOUtilities.close(br);
      }

      int size = ra.length();
      char[] var18 = new char[size];
      char[] var19 = new char[size];
      char[] var20 = new char[size];
      ra.getChars(0, size, var18, 0);
      rb.getChars(0, size, var19, 0);
      rc.getChars(0, size, var20, 0);
      return new CharacterMergeRulesConfiguration(var18, var19, var20);
   }
}
