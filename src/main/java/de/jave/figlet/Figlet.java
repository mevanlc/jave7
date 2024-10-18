package de.jave.figlet;

import de.jave.figlet.engine.FigDriver;
import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.file.BaseFolderFigFileResource;
import de.jave.figlet.file.BaseUrlFigFileResource;
import de.jave.figlet.file.ClasspathFigFileResource;
import de.jave.figlet.file.IFigFileLibrary;
import de.jave.figlet.file.IFigFileResource;
import de.jave.figlet.file.ZipFileFigFileResource;
import de.jave.figlet.util.FigException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import net.disy.commons.core.util.Ensure;

public class Figlet {
   private final IFigDriver driver;

   public Figlet() throws FigException {
      this(new ClasspathFigFileResource());
   }

   public Figlet(String libraryLocation) throws FigException {
      this(createFigFileResource(libraryLocation));
   }

   public Figlet(File file) throws FigException {
      this(createFigFileResource(file));
   }

   private static IFigFileResource createFigFileResource(String libraryLocation) throws FigException {
      if (libraryLocation != null && libraryLocation.length() != 0) {
         if (!libraryLocation.toLowerCase().startsWith("http:") && !libraryLocation.toLowerCase().startsWith("file:")) {
            return createFigFileResource(new File(libraryLocation));
         } else {
            try {
               return new BaseUrlFigFileResource(new URL(libraryLocation));
            } catch (MalformedURLException var2) {
               throw new FigException("The given library location '" + libraryLocation + "' does not seem to be a valid URL", var2);
            }
         }
      } else {
         return new ClasspathFigFileResource();
      }
   }

   private static IFigFileResource createFigFileResource(File file) throws FigException {
      return file.exists() && !file.isDirectory() ? new ZipFileFigFileResource(file) : new BaseFolderFigFileResource(file);
   }

   public Figlet(URL fontFolderBaseUrl) throws FigException {
      this(new BaseUrlFigFileResource(fontFolderBaseUrl));
   }

   public Figlet(IFigFileResource fileResource) throws FigException {
      this.driver = new FigDriver(fileResource);
   }

   public String figletize(String text) throws FigException {
      return this.figletize(text, "standard");
   }

   public String figletize(String text, String fontName) throws FigException {
      Ensure.ensureArgumentNotNull(text);
      Ensure.ensureArgumentNotNull(fontName);
      return this.driver.figletize(text, fontName);
   }

   public String figletizeFigml(String figmlText) throws FigException {
      Ensure.ensureArgumentNotNull(figmlText);
      return this.driver.figml(figmlText);
   }

   public IFigDriver getFigDriver() {
      return this.driver;
   }

   public IFigFileLibrary getFileLibrary() {
      return this.getFigDriver().getFileLibrary();
   }
}
