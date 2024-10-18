package de.jave.figlet.util;

import de.jave.figlet.file.IFigFontCategorization;
import de.jave.figlet.file.IFigFontCategory;
import de.jave.figlet.file.IFigFontCategoryContainer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FigUtilities {
   private FigUtilities() {
   }

   public static int stringToInt(String line) throws NumberFormatException {
      int base = 10;
      if (line.startsWith("0x")) {
         base = 16;
         line = line.substring(2);
      }

      return Integer.parseInt(line, base);
   }

   public static IFigFontCategory[] getAllCategories(IFigFontCategorization categorization) {
      return getAllCategoriesInternal(categorization);
   }

   private static IFigFontCategory[] getAllCategoriesInternal(IFigFontCategoryContainer categorization) {
      List<IFigFontCategory> all = new ArrayList<>();
      IFigFontCategory[] categories = categorization.getChildCategories();

      for (int i = 0; i < categories.length; i++) {
         all.add(categories[i]);
         all.addAll(Arrays.asList(getAllCategoriesInternal(categories[i])));
      }

      return all.toArray(new IFigFontCategory[0]);
   }

   public static IFigFontCategory[] getCategoriesContainingFont(IFigFontCategorization categorization, String fontName) {
      IFigFontCategory[] categories = getAllCategories(categorization);
      List<IFigFontCategory> result = new ArrayList<>();

      for (int i = 0; i < categories.length; i++) {
         boolean contained = categories[i].containsFont(fontName);
         if (contained) {
            result.add(categories[i]);
         }
      }

      return result.toArray(new IFigFontCategory[0]);
   }

   public static IFigFontCategory getCategory(IFigFontCategorization categorization, String categoryName) {
      IFigFontCategory[] categories = getAllCategories(categorization);

      for (int i = 0; i < categories.length; i++) {
         IFigFontCategory category = categories[i];
         if (categoryName.equals(category.getName())) {
            return category;
         }
      }

      return null;
   }
}
