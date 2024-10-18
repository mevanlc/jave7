package de.jave.figlet.file;

import de.jave.lib.IPublicCloneable;

public interface IFigFontCategory extends IFigFontCategoryContainer, IPublicCloneable {
   String getName();

   int getFontCount();

   String[] getFontNames();

   void addFontName(String var1);

   boolean containsFont(String var1);

   void removeFontName(String var1);

   String getDescription();

   void setDescription(String var1);

   boolean isDynamicallyGenerated();
}
