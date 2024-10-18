package de.jave.figlet.file;

import de.jave.figlet.util.FigException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;

public class BaseFolderFigFileResource extends AbstractFigFileResource {
   private final File folder;

   public BaseFolderFigFileResource(File folder) throws FigException {
      if (!folder.exists()) {
         throw new FigException("Base folder for FIGlet files does not exist: '" + folder.getAbsolutePath() + "'");
      } else if (!folder.isDirectory()) {
         throw new FigException("Base folder for FIGlet files is not a directory: '" + folder.getAbsolutePath() + "'");
      } else if (!folder.canRead()) {
         throw new FigException("Base folder for FIGlet files can not be read: '" + folder.getAbsolutePath() + "'");
      } else {
         this.folder = folder;
      }
   }

   @Override
   public InputStream openConfigurationFileInputStream(String fileName) {
      try {
         return this.openInputStream(fileName);
      } catch (FileNotFoundException var3) {
         return null;
      }
   }

   private InputStream openInputStream(String filename) throws FileNotFoundException {
      File file = new File(this.folder, filename);
      return new FileInputStream(file);
   }

   @Override
   public long getLastModified(FigFileName fileName) {
      File file = new File(this.folder, fileName.getName());
      return file.lastModified();
   }

   @Override
   public FigFileName[] loadFigletFileNames() throws FigException {
      String[] names = this.folder.list(new FilenameFilter() {
         @Override
         public boolean accept(File dir, String name) {
            String lowerCaseName = name.toLowerCase();
            return lowerCaseName.endsWith(".flf") || lowerCaseName.endsWith(".flc");
         }
      });
      if (names == null) {
         throw new FigException("Unable to list directory for fonts: '" + this.folder.getAbsolutePath() + "'");
      } else {
         return this.createFigFileNames(names);
      }
   }

   @Override
   public InputStream openFigFileInputStream(FigFileName fileDescription) throws FigException {
      try {
         return this.openInputStream(fileDescription.getName());
      } catch (FileNotFoundException var3) {
         throw new FigException("Requested file '" + fileDescription.getName() + "' does not exist (" + this.getRourceBaseDescription() + ").");
      }
   }

   @Override
   public String getRourceBaseDescription() {
      return "folder '" + this.folder.getAbsolutePath() + "'";
   }

   public File getBaseFolder() {
      return this.folder;
   }
}
