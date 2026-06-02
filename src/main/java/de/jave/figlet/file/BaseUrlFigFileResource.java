package de.jave.figlet.file;

import de.jave.figlet.util.FigException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import net.dizzy.commons.core.util.Ensure;

public class BaseUrlFigFileResource extends AbstractFigFileResource {
   private final URL baseUrl;

   public BaseUrlFigFileResource(URL baseUrl) {
      Ensure.ensureArgumentNotNull(baseUrl);
      this.baseUrl = baseUrl;
   }

   @Override
   public InputStream openConfigurationFileInputStream(String fileName) throws FigException {
      return this.openFileInputStream(fileName);
   }

   private InputStream openFileInputStream(String inputFileName) throws FigException {
      try {
         URL url = new URL(this.baseUrl, inputFileName);
         return url.openStream();
      } catch (MalformedURLException var3) {
         throw new FigException("Unable to create url for input file '" + inputFileName + "'", var3);
      } catch (IOException var4) {
         throw new FigException("Unable to connect to url for input file '" + inputFileName + "'", var4);
      }
   }

   @Override
   public long getLastModified(FigFileName fileName) throws FigException {
      String inputFileName = fileName.getName();

      URL url;
      try {
         url = new URL(this.baseUrl, inputFileName);
      } catch (MalformedURLException var5) {
         throw new RuntimeException(var5);
      }

      return this.getLastModifiedDate(url);
   }

   @Override
   public FigFileName[] loadFigletFileNames() throws FigException {
      return this.loadFigletFileNames(this.openFileInputStream("files.txt"));
   }

   @Override
   public InputStream openFigFileInputStream(FigFileName fileDescription) throws FigException {
      return this.openFileInputStream(fileDescription.getName());
   }

   @Override
   public String getRourceBaseDescription() {
      return "Url '" + this.baseUrl.toExternalForm() + "'";
   }
}
