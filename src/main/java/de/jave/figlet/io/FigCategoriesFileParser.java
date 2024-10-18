package de.jave.figlet.io;

import de.jave.figlet.file.FigFontCategorization;
import de.jave.figlet.file.FigFontCategory;
import de.jave.figlet.file.IFigFileResource;
import de.jave.figlet.file.IFigFontCategory;
import de.jave.figlet.file.IFigFontCategoryContainer;
import de.jave.figlet.util.FigException;
import de.jave.figlet.util.FigFileFormatException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import net.disy.commons.core.io.IOUtilities;

public class FigCategoriesFileParser {
   private static final String NEW_CATEGORY_NAME = "New fonts";
   private static final String CATEGORY_DEFAULT = "Outline";

   public FigFontCategorization loadCategoriesFile(String[] fontNames, IFigFileResource fileResource, String categoriesFileName) throws FigException {
      HashSet<String> unusedFontNames = new HashSet<>(Arrays.asList(fontNames));
      HashSet<String> allFontNames = new HashSet<>(Arrays.asList(fontNames));
      FigFontCategorization categorization = null;
      InputStream inputStream = null;
      BufferedReader reader = null;

      try {
         inputStream = fileResource.openConfigurationFileInputStream(categoriesFileName);
         if (inputStream == null) {
            throw new FigException("No categories file '" + categoriesFileName + "' existing (" + fileResource.getRourceBaseDescription() + ")");
         }

         categorization = new FigFontCategorization();
         String defaultFontCategoryName = null;
         reader = new BufferedReader(new InputStreamReader(inputStream));
         FigFontCategory currentCategory = null;

         String line;
         while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() >= 1 && line.charAt(0) != '#') {
               if (line.charAt(0) == '[') {
                  String[] categoryNamePath = this.parseCategoryTreePath(line);
                  String description = this.parseDescription(line);
                  currentCategory = this.addCategory(categorization, categoryNamePath);
                  currentCategory.setDescription(description);
                  if (defaultFontCategoryName == null || currentCategory.getName().equals("Outline")) {
                     defaultFontCategoryName = currentCategory.getName();
                  }
               } else if (allFontNames.contains(line)) {
                  unusedFontNames.remove(line);
                  if (currentCategory == null) {
                     throw new FigFileFormatException("No category defined for font '" + line + "'");
                  }

                  currentCategory.addFontName(line);
               }
            }
         }

         if (unusedFontNames.size() > 0) {
            currentCategory = new FigFontCategory("New fonts");
            currentCategory.setDynamicallyGenerated(true);

            for (String element : unusedFontNames) {
               currentCategory.addFontName(element);
            }

            categorization.addChild(currentCategory);
         }

         currentCategory = new FigFontCategory("All fonts");
         currentCategory.setDynamicallyGenerated(true);

         for (int i = 0; i < fontNames.length; i++) {
            currentCategory.addFontName(fontNames[i]);
         }

         categorization.addChild(currentCategory);
         categorization.setDefaultCategoryName(defaultFontCategoryName);
      } catch (IOException var17) {
         throw new FigException("Error reading categories file (" + fileResource.getRourceBaseDescription() + ")", var17);
      } finally {
         IOUtilities.close(reader);
         IOUtilities.close(inputStream);
      }

      return categorization;
   }

   public FigFontCategory addCategory(FigFontCategorization categorization, String[] categoryNamePath) {
      IFigFontCategoryContainer parent = categorization;

      for (int i = 0; i < categoryNamePath.length; i++) {
         IFigFontCategory child = parent.getChildCategory(categoryNamePath[i]);
         if (child == null) {
            child = new FigFontCategory(categoryNamePath[i]);
            parent.addChild(child);
         }

         parent = child;
      }

      return (FigFontCategory)parent;
   }

   public String[] parseCategoryTreePath(String line) throws FigFileFormatException {
      if (line.startsWith("[") && line.indexOf("]") != -1) {
         line = line.substring(1, line.indexOf(93));
         List<String> pathelements = new ArrayList<>();
         int startIndex = 0;
         int endIndex = -1;

         while (true) {
            startIndex = endIndex + 1;
            endIndex = line.indexOf(124, startIndex);
            if (endIndex == -1) {
               pathelements.add(line.substring(startIndex));
               return pathelements.toArray(new String[0]);
            }

            pathelements.add(line.substring(startIndex, endIndex));
         }
      } else {
         throw new FigFileFormatException("Illegal format for category specification '" + line + "'");
      }
   }

   private String parseDescription(String line) {
      int startIndex = line.indexOf(93);
      int i1 = line.indexOf("{", startIndex);
      if (i1 == -1) {
         return null;
      } else {
         int i2 = line.indexOf("}", i1);
         return i2 == -1 ? null : line.substring(i1 + 1, i2);
      }
   }
}
