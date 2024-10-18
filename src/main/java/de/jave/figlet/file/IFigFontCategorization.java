package de.jave.figlet.file;

import de.jave.lib.IPublicCloneable;

public interface IFigFontCategorization extends IFigFontCategoryContainer, IPublicCloneable {
   IFigFontCategory[] getAllNonEmptyCategories();

   IFigFontCategory getDefaultCategory();
}
