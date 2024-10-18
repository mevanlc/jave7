package de.jave.figlet.file;

public interface IFigFontCategoryContainer {
   int getChildCategoryCount();

   IFigFontCategory[] getChildCategories();

   void addChild(IFigFontCategory var1);

   IFigFontCategory getChildCategory(String var1);
}
