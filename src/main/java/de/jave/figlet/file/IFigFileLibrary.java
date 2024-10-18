package de.jave.figlet.file;

import de.jave.figlet.util.FigException;

public interface IFigFileLibrary {
   String[] getAllFontNames();

   String getDefaultFontName();

   IFigFontCategorization getFontCategorization();

   IFigFontCategory getFontCategory(String var1);

   IFigFileResource getFileResource();

   void initialize() throws FigException;
}
