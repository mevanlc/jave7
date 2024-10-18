package de.jave.figlet.file;

import de.jave.figlet.util.FigException;
import java.io.InputStream;

public interface IFigFileResource {
   String CATEGORY_FILE_NAME = "categoriestree.txt";
   String OLD_CATEGORY_FILE_NAME = "categories.txt";
   String FILELIST_FILE_NAME = "files.txt";

   FigFileName[] loadFigletFileNames() throws FigException;

   InputStream openConfigurationFileInputStream(String var1) throws FigException;

   InputStream openFigFileInputStream(FigFileName var1) throws FigException;

   String getRourceBaseDescription();

   long getLastModified(FigFileName var1) throws FigException;
}
