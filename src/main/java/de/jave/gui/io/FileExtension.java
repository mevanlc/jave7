package de.jave.gui.io;

import java.io.File;
import net.dizzy.commons.core.util.Ensure;

public class FileExtension implements Comparable<FileExtension> {
   private final String string;

   public FileExtension(String string) {
      Ensure.ensureArgumentNotNull(string);
      this.string = string;
   }

   @Override
   public int hashCode() {
      return this.string.hashCode();
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof FileExtension)) {
         return false;
      } else {
         FileExtension other = (FileExtension)obj;
         return this.string.equals(other.string);
      }
   }

   public String getString() {
      return this.string;
   }

   public int compareTo(FileExtension o) {
      return this.string.compareTo(o.string);
   }

   public static FileExtension getFrom(File file) {
      String name = file.getName();
      int index = name.lastIndexOf(46);
      return index == -1 ? new FileExtension("") : new FileExtension(name.substring(index + 1).toLowerCase());
   }
}
