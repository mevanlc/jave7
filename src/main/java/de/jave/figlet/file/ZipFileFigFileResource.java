package de.jave.figlet.file;

import de.jave.figlet.util.FigException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileFigFileResource extends AbstractFigFileResource {
   private final File zipFile;

   public ZipFileFigFileResource(File zipFile) throws FigException {
      if (!zipFile.exists()) {
         throw new FigException("Zip file for figlet files does not exist: '" + zipFile.getAbsolutePath() + "'");
      } else if (!zipFile.isFile()) {
         throw new FigException("Zip file for figlet files is not a file: '" + zipFile.getAbsolutePath() + "'");
      } else if (!zipFile.canRead()) {
         throw new FigException("Zip file for figlet files can not be read: '" + zipFile.getAbsolutePath() + "'");
      } else {
         this.zipFile = zipFile;
      }
   }

   @Override
   public String getRourceBaseDescription() {
      return "Zip " + this.zipFile.getAbsolutePath();
   }

   @Override
   public FigFileName[] loadFigletFileNames() throws FigException {
      try {
         ZipFile file = new ZipFile(this.zipFile);
         Enumeration<? extends ZipEntry> enumeration = file.entries();
         List<FigFileName> list = new ArrayList<>();

         while (enumeration.hasMoreElements()) {
            ZipEntry element = enumeration.nextElement();
            String lowerCaseEntryName = element.getName().toLowerCase();
            if (lowerCaseEntryName.endsWith(".flf") || lowerCaseEntryName.endsWith(".flf")) {
               list.add(new FigFileName(element.getName()));
            }
         }

         file.close();
         return list.toArray(new FigFileName[0]);
      } catch (IOException var6) {
         throw new FigException("Unable to list files " + this.getRourceBaseDescription(), var6);
      }
   }

   @Override
   public InputStream openConfigurationFileInputStream(String fileName) throws FigException {
      try (ZipFile file = new ZipFile(this.zipFile);) {
         ZipEntry entry = file.getEntry("fonts/" + fileName);
         return entry == null ? null : file.getInputStream(entry);
      } catch (IOException var4) {
         throw new FigException("The requested file '" + fileName + "' can not be read " + this.getRourceBaseDescription(), var4);
      }
   }

   @Override
   public InputStream openFigFileInputStream(FigFileName fileName) throws FigException {
      try (ZipFile file = new ZipFile(this.zipFile);) {
         ZipEntry entry = file.getEntry("fonts/" + fileName);
         if (entry == null) {
            throw new FigException("The requested file '" + fileName + "' does not exist " + this.getRourceBaseDescription());
         } else {
            return file.getInputStream(entry);
         }
      } catch (IOException var4) {
         throw new FigException("The requested file '" + fileName + "' can not be read " + this.getRourceBaseDescription(), var4);
      }
   }

   @Override
   public long getLastModified(FigFileName fileName) throws FigException {
      try (ZipFile file = new ZipFile(this.zipFile);) {

         ZipEntry entry = file.getEntry("fonts/" + fileName);
         if (entry == null) {
            throw new FigException("The requested file '" + fileName + "' does not exist " + this.getRourceBaseDescription());
         } else {
            return entry.getTime();
         }
      } catch (IOException var4) {
         throw new FigException("The requested file '" + fileName + "' can not be read " + this.getRourceBaseDescription(), var4);
      }
   }
}
