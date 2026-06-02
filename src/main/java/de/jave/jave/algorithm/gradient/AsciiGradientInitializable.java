package de.jave.jave.algorithm.gradient;

import de.jave.jave.configuration.ConfigurationException;
import de.jave.jave.configuration.IJavaInitializationContext;
import de.jave.jave.configuration.IJaveInitializable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.dizzy.commons.core.io.IOUtilities;

public class AsciiGradientInitializable implements IJaveInitializable<AsciiGradientConfiguration> {
   public AsciiGradientConfiguration initialize(IJavaInitializationContext context) throws ConfigurationException {
      File configurationFile = context.getConfigurationFile("./config/gradients.txt");
      List<String> lines = new ArrayList<>();
      BufferedReader reader = null;

      try {
         reader = new BufferedReader(new FileReader(configurationFile));
         String line = null;

         while ((line = reader.readLine()) != null) {
            if (line.length() > 0) {
               lines.add(line);
            }
         }
      } catch (IOException var9) {
         throw new ConfigurationException(configurationFile, var9);
      } finally {
         IOUtilities.close(reader);
      }

      int size = lines.size();
      if (size == 0) {
         throw new ConfigurationException(configurationFile, "The file seems to be empty.");
      } else {
         String[] gradients = lines.toArray(new String[0]);
         return new AsciiGradientConfiguration(gradients);
      }
   }
}
