package de.jave.gui.io;

import java.io.File;

public class FileSelection {
   private final File[] files;

   public FileSelection(File[] files) {
      this.files = files;
   }

   public FileSelection(File file) {
      this(toFileArray(file));
   }

   private static File[] toFileArray(File file) {
      return file == null ? new File[0] : new File[]{file};
   }

   public File getFile() {
      return this.files.length > 0 ? this.files[0] : null;
   }

   public File[] getFiles() {
      return this.files;
   }

   public boolean isEmpty() {
      return this.files.length == 0;
   }

   public static FileSelection createEmpty() {
      return new FileSelection(new File[0]);
   }
}
