package de.jave.image2ascii;

import de.jave.jave.configuration.ConfigurationException;
import de.jave.jave.configuration.IJavaInitializationContext;
import de.jave.jave.configuration.IJaveInitializable;
import de.jave.text.TextTools;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AsciiGreyscaleTableConfigurationInitializable implements IJaveInitializable<AsciiGreyscaleTableConfiguration> {
   public AsciiGreyscaleTableConfiguration initialize(IJavaInitializationContext context) throws ConfigurationException {
      File configurationFile = context.getConfigurationFile("./config/greyscaletables/");
      if (!configurationFile.isDirectory()) {
         throw new ConfigurationException(configurationFile, "There is no such folder containing cofiguration files.");
      } else {
         String[] fileNames = configurationFile.list();
         int counter = 0;

         for (int i = 0; i < fileNames.length; i++) {
            if (fileNames[i].toLowerCase().endsWith(".jgt")) {
               counter++;
            }
         }

         if (counter == 0) {
            throw new ConfigurationException(configurationFile, "There are no configuration files in this folder.");
         } else {
            String[] tableNames = new String[counter];
            String defaultTableName = null;
            Map<String, AsciiGreyScaleTableItem> tableItemsByName = new HashMap<>();
            int index = 0;

            for (int ix = 0; ix < fileNames.length; ix++) {
               if (fileNames[ix].toLowerCase().endsWith(".jgt")) {
                  tableNames[index] = fileNames[ix].substring(0, fileNames[ix].length() - 4);
                  if (fileNames[ix].charAt(0) == '_') {
                     tableNames[index] = tableNames[index].substring(1);
                     tableNames[index] = TextTools.firstLetterUp(tableNames[index]);
                     defaultTableName = tableNames[index];
                  } else {
                     tableNames[index] = TextTools.firstLetterUp(tableNames[index]);
                  }

                  File f = new File(configurationFile, fileNames[ix]);
                  AsciiGreyscaleTable newTable = AsciiGreyscaleTableReader.load(f);
                  tableItemsByName.put(tableNames[index], new AsciiGreyScaleTableItem(tableNames[index], newTable));
                  index++;
               }
            }

            Arrays.sort(tableNames);
            if (defaultTableName == null) {
               defaultTableName = tableNames[0];
            }

            return new AsciiGreyscaleTableConfiguration(tableNames, defaultTableName, tableItemsByName);
         }
      }
   }
}
