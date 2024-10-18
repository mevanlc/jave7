package de.jave.figlet.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.disy.commons.core.util.Ensure;

public class FigFontCategorization implements IFigFontCategorization {
   public static final String ALL_CATEGORY_NAME = "All fonts";
   private String defaultCategoryName = "All fonts";
   private final List<IFigFontCategory> categories = new ArrayList<>();
   private final Map<String, IFigFontCategory> categoriesByName = new HashMap<>();

   public static FigFontCategorization createFallBackCategorization(String[] fontNames) {
      String name = "All fonts";
      FigFontCategory singleCategory = new FigFontCategory("All fonts");
      singleCategory.addFontNames(fontNames);
      FigFontCategorization categorization = new FigFontCategorization();
      categorization.addChild(singleCategory);
      categorization.setDefaultCategoryName("All fonts");
      return categorization;
   }

   @Override
   public Object clone() {
      FigFontCategorization clone = new FigFontCategorization();
      clone.setDefaultCategoryName(this.defaultCategoryName);
      IFigFontCategory[] childCategories = this.getChildCategories();

      for (int i = 0; i < this.getChildCategoryCount(); i++) {
         clone.addChild((IFigFontCategory)childCategories[i].clone());
      }

      return clone;
   }

   public void setDefaultCategoryName(String name) {
      Ensure.ensureArgumentNotNull(name);
      this.defaultCategoryName = name;
   }

   @Override
   public IFigFontCategory[] getAllNonEmptyCategories() {
      List<IFigFontCategory> nonEmpty = new ArrayList<>();

      for (int i = 0; i < this.categories.size(); i++) {
         IFigFontCategory category = this.categories.get(i);
         if (category.getFontCount() > 0) {
            nonEmpty.add(category);
         }
      }

      return nonEmpty.toArray(new IFigFontCategory[0]);
   }

   @Override
   public IFigFontCategory getDefaultCategory() {
      return this.getCategory(this.defaultCategoryName);
   }

   public IFigFontCategory getCategory(String name) {
      return getCategory(this, name);
   }

   private static IFigFontCategory getCategory(IFigFontCategoryContainer container, String name) {
      if (container instanceof IFigFontCategory && ((IFigFontCategory)container).getName().equals(name)) {
         return (IFigFontCategory)container;
      } else {
         IFigFontCategory[] childCategories = container.getChildCategories();

         for (int i = 0; i < childCategories.length; i++) {
            IFigFontCategory category = getCategory(childCategories[i], name);
            if (category != null) {
               return category;
            }
         }

         return null;
      }
   }

   public int getCategoryCount() {
      return this.categories.size();
   }

   @Override
   public int getChildCategoryCount() {
      return this.categories.size();
   }

   @Override
   public IFigFontCategory[] getChildCategories() {
      return this.categories.toArray(new IFigFontCategory[0]);
   }

   @Override
   public void addChild(IFigFontCategory child) {
      this.categories.add(child);
      this.categoriesByName.put(child.getName(), child);
   }

   @Override
   public IFigFontCategory getChildCategory(String name) {
      for (IFigFontCategory element : this.categories) {
         if (element.getName().equals(name)) {
            return element;
         }
      }

      return null;
   }
}
