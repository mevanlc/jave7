package net.disy.commons.swing.filechooser.util;

import java.io.File;
import java.util.Comparator;
import javax.swing.filechooser.FileSystemView;
import net.disy.commons.core.util.Ensure;

public class FileComparator implements Comparator<File> {
   private final FileSystemView fileSystemView;

   public FileComparator(FileSystemView fileSystemView) {
      Ensure.ensureArgumentNotNull(fileSystemView);
      this.fileSystemView = fileSystemView;
   }

   public int compare(File file1, File file2) {
      if (file1.isDirectory() && !file2.isDirectory()) {
         return -1;
      } else if (!file1.isDirectory() && file2.isDirectory()) {
         return 1;
      } else {
         String displayName1 = this.getDisplayName(file1);
         String displayName2 = this.getDisplayName(file2);
         return displayName1.compareToIgnoreCase(displayName2);
      }
   }

   private String getDisplayName(File file) {
      return this.fileSystemView.isDrive(file) ? file.getName() : this.fileSystemView.getSystemDisplayName(file);
   }
}
