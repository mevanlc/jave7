package de.jave.jave.algorithm.replaceillegal;

import de.jave.jave.configuration.ConfigurationException;
import de.jave.jave.configuration.IJavaInitializationContext;
import de.jave.jave.configuration.IJaveInitializable;
import de.jave.lib.collections.IntVector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import net.disy.commons.core.io.IOUtilities;

public class AsciiReplaceIllegalConfigurationInitializable implements IJaveInitializable<AsciiReplaceIllegalConfiguration> {
   public AsciiReplaceIllegalConfiguration initialize(IJavaInitializationContext context) throws ConfigurationException {
      File configurationFile = context.getConfigurationFile("./config/replace_illegal.txt");
      IntVector source = new IntVector(100);
      IntVector destination = new IntVector(100);
      BufferedReader br = null;

      try {
         br = new BufferedReader(new FileReader(configurationFile));
         String line = null;

         while ((line = br.readLine()) != null) {
            if (line.length() > 2 && line.charAt(0) != '#') {
               int i1 = line.indexOf(32);
               if (i1 == -1) {
                  throw new Exception("The file has a syntax error: '" + line + "'");
               }

               try {
                  source.addElement(Integer.parseInt(line.substring(0, i1)));
               } catch (Exception var13) {
                  throw new Exception("The file has a syntax error: '" + line + "' does not start with a number.");
               }

               destination.addElement(line.charAt(i1 + 1));
            }
         }
      } catch (Exception var14) {
         throw new ConfigurationException(configurationFile, var14);
      } finally {
         IOUtilities.close(br);
      }

      int var17 = source.size();
      if (source.size() == 0) {
         throw new ConfigurationException(configurationFile, "The file seems to be empty.");
      } else {
         char[] replaceIllegalSource = new char[var17];
         char[] replaceIllegalDestination = new char[var17];

         for (int i = 0; i < var17; i++) {
            replaceIllegalSource[i] = (char)source.elementAt(i);
            replaceIllegalDestination[i] = (char)destination.elementAt(i);
         }

         return new AsciiReplaceIllegalConfiguration(replaceIllegalSource, replaceIllegalDestination);
      }
   }
}
