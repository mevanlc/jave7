package de.jave.figlet.file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FigFontCategory implements IFigFontCategory {
   private final String name;
   private String description;
   private List<String> fontNames = new ArrayList<>();
   private final List<IFigFontCategory> children = new ArrayList<>();
   private boolean isDynamicallyGenerated;

   public FigFontCategory(String name) {
      this.name = name;
   }

   public void setDynamicallyGenerated(boolean isDynamicallyGenerated) {
      this.isDynamicallyGenerated = isDynamicallyGenerated;
   }

   @Override
   public Object clone() {
      FigFontCategory clone = new FigFontCategory(this.name);
      clone.setDynamicallyGenerated(this.isDynamicallyGenerated());
      clone.setDescription(this.getDescription());
      IFigFontCategory[] childCategories = this.getChildCategories();

      for (int i = 0; i < childCategories.length; i++) {
         clone.addChild((IFigFontCategory)childCategories[i].clone());
      }

      clone.fontNames = new ArrayList<>(this.fontNames);
      return clone;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String getDescription() {
      return this.description;
   }

   @Override
   public void setDescription(String description) {
      this.description = description;
   }

   public String getNameWithSize() {
      return this.name + " (" + this.getFontCount() + ")";
   }

   @Override
   public void addFontName(String fontName) {
      this.fontNames.add(fontName);
   }

   @Override
   public void removeFontName(String fontName) {
      this.fontNames.remove(fontName);
   }

   public void addFontNames(String[] fontNames) {
      this.fontNames.addAll(Arrays.asList(fontNames));
   }

   @Override
   public int getFontCount() {
      return this.fontNames.size();
   }

   @Override
   public String[] getFontNames() {
      String[] names = this.fontNames.toArray(new String[this.getFontCount()]);
      Arrays.sort(names);
      return names;
   }

   @Override
   public int getChildCategoryCount() {
      return this.children.size();
   }

   @Override
   public IFigFontCategory[] getChildCategories() {
      return this.children.toArray(new IFigFontCategory[0]);
   }

   @Override
   public void addChild(IFigFontCategory child) {
      this.children.add(child);
   }

   @Override
   public IFigFontCategory getChildCategory(String name) {
      for (IFigFontCategory element : this.children) {
         if (element.getName().equals(name)) {
            return element;
         }
      }

      return null;
   }

   @Override
   public boolean containsFont(String fontName) {
      return this.fontNames.contains(fontName);
   }

   @Override
   public boolean isDynamicallyGenerated() {
      return this.isDynamicallyGenerated;
   }
}
