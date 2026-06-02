package de.jave.jave.filter;

import de.jave.jave.configuration.ConfigurationException;
import de.jave.jave.configuration.IJavaInitializationContext;
import de.jave.jave.configuration.IJaveInitializable;
import de.jave.lib.CharacterPlate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.dizzy.commons.core.io.IOUtilities;
import net.dizzy.commons.core.util.Ensure;

public class FilterInitializable implements IJaveInitializable<Filter> {
   public Filter initialize(IJavaInitializationContext context) throws ConfigurationException {
      Ensure.ensureArgumentNotNull(context);
      FilterMatrix[] postFilters = this.convertFilters(context, "post.filter");
      FilterMatrix[] preFilters = this.convertFilters(context, "pre.filter");
      FilterMatrix[] midFilters = this.convertFilters(context, "mid.filter");
      FilterMatrix[] lineArtCleanerFilters = this.convertFilters(context, "lineartcleaner.filter");
      FilterMatrix[] image2asciiSimpleEdgeFilters = this.convertFilters(context, "i2asimpleedgedetect.filter");
      return new Filter(preFilters, midFilters, postFilters, lineArtCleanerFilters, image2asciiSimpleEdgeFilters);
   }

   private FilterMatrix[] convertFilters(IJavaInitializationContext context, String fileName) throws ConfigurationException {
      File file = context.getConfigurationFile("config/filter/" + fileName);
      BufferedReader reader = null;
      List<FilterMatrix> matrices = new ArrayList<>();

      try {
         FileReader fileReader = new FileReader(file);
         reader = new BufferedReader(fileReader);
         CharacterPlate plate = null;

         while (true) {
            String line = reader.readLine();
            if (line == null || line.length() == 0) {
               matrices.add(this.parseMatrix(plate));
               plate = null;
               if (line == null) {
                  return matrices.toArray(new FilterMatrix[0]);
               }
            } else if (plate == null) {
               plate = new CharacterPlate(line);
            } else {
               plate.addLinesBottom(1);
               plate.paste(line, 0, plate.getHeight() - 1);
            }
         }
      } catch (FileNotFoundException var13) {
         throw new ConfigurationException(file, "Configuration file not found", var13);
      } catch (IOException var14) {
         throw new ConfigurationException(file, "Error reading configuration file", var14);
      } finally {
         IOUtilities.close(reader);
      }
   }

   private FilterMatrix parseMatrix(CharacterPlate plate) {
      int width = (plate.getWidth() - 1) / 2;
      int height = plate.getHeight();
      CharacterPlate mask = plate.getCopy(0, 0, width, height);
      CharacterPlate result = plate.getCopy(width + 1, 0, width, height);
      return new FilterMatrix(mask, result);
   }
}
