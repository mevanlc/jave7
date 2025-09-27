package net.disy.commons.swing.filechooser.view;

import java.io.File;
import javax.swing.filechooser.FileSystemView;

public class FileSystemViewUtilities {
   public static File[] listFiles(FileSystemView fileSystemView, File folder) {
      try {
         return fileSystemView.getFiles(folder, true);
      } catch (InternalError var3) {
         System.err.println("Unable to expand folder '" + folder.getAbsolutePath() + "' - ignored");
         var3.printStackTrace();
         return new File[0];
      } catch (NullPointerException var4) {
         System.err.println("Unable to expand folder '" + folder.getAbsolutePath() + "' - ignored");
         var4.printStackTrace();
         return new File[0];
      }
   }
}
