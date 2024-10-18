package de.jave.figlet.file;

import de.jave.figlet.util.FigException;
import java.io.InputStream;
import java.net.URL;

public class ClasspathFigFileResource extends AbstractFigFileResource {
   @Override
   public InputStream openConfigurationFileInputStream(String fileName) {
      return this.openFileInputStream(fileName);
   }

   private InputStream openFileInputStream(String fileName) {
      String name = "/" + fileName;
      return this.getClass().getResourceAsStream(name);
   }

   @Override
   public long getLastModified(FigFileName fileName) throws FigException {
      URL url = this.getClass().getResource("/" + fileName.getName());
      return this.getLastModifiedDate(url);
   }

   @Override
   public FigFileName[] loadFigletFileNames() throws FigException {
      return this.loadFigletFileNames(this.openFileInputStream("files.txt"));
   }

   @Override
   public InputStream openFigFileInputStream(FigFileName fileDescription) {
      return this.openFileInputStream(fileDescription.getName());
   }

   @Override
   public String getRourceBaseDescription() {
      return "Classpath";
   }
}
