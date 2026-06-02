package de.jave.figlet.file;

import de.jave.figlet.util.FigException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import net.dizzy.commons.core.io.IOUtilities;

public abstract class AbstractFigFileResource implements IFigFileResource {
   protected final FigFileName[] createFigFileNames(String[] names) {
      FigFileName[] figFileNames = new FigFileName[names.length];

      for (int i = 0; i < figFileNames.length; i++) {
         figFileNames[i] = new FigFileName(names[i]);
      }

      return figFileNames;
   }

   protected final FigFileName[] loadFigletFileNames(InputStream inputStream) throws FigException {
      List<FigFileName> filenames = new ArrayList<>();
      BufferedReader reader = null;

      try {
         if (inputStream == null) {
            throw new FigException("Unable to load font list (" + this.getRourceBaseDescription() + ")", 7);
         }

         reader = new BufferedReader(new InputStreamReader(inputStream));
         String line = null;

         while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() != 0 && !line.startsWith("#")) {
               FigFileName fontDescription = new FigFileName(line);
               filenames.add(fontDescription);
            }
         }
      } catch (IOException var9) {
         throw new FigException("Unable to load index file containing a list of all font/control files (" + this.getRourceBaseDescription() + ")", var9, 1);
      } finally {
         IOUtilities.close(reader);
         IOUtilities.close(inputStream);
      }

      return filenames.toArray(new FigFileName[0]);
   }

   protected final long getLastModifiedDate(URL url) throws FigException {
      try {
         URLConnection connection = url.openConnection();
         return connection.getLastModified();
      } catch (IOException var4) {
         throw new FigException("Unable to connect to url", var4);
      }
   }
}
