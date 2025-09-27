package net.disy.commons.swing.filechooser.util;

import java.io.File;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.icon.SwingIcons;
import net.disy.commons.swing.ui.AbstractObjectUi;

public class FileObjectUi extends AbstractObjectUi<File> {
   private final FileSystemView fileSystemView;

   public FileObjectUi() {
      this(FileSystemView.getFileSystemView());
   }

   public FileObjectUi(FileSystemView fileSystemView) {
      Ensure.ensureArgumentNotNull(fileSystemView);
      this.fileSystemView = fileSystemView;
   }

   public Icon getIcon(File value) {
      try {
         return this.fileSystemView.getSystemIcon(value);
      } catch (NullPointerException var3) {
         System.err.println("Unable to get system icon '" + value.getAbsolutePath() + "' - ignored");
         var3.printStackTrace();
         return SwingIcons.getFileViewFileIcon();
      }
   }

   public String getLabel(File value) {
      return this.fileSystemView.getSystemDisplayName(value);
   }
}
