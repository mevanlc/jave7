package net.dizzy.commons.swing.icon;

import javax.swing.Icon;
import javax.swing.UIManager;

public final class CommonIcons {
   public static final Icon FOLDER = icon("FileView.directoryIcon");
   public static final Icon FOLDER_NEW = icon("FileChooser.newFolderIcon");

   private CommonIcons() {
   }

   private static Icon icon(String key) {
      Icon icon = UIManager.getIcon(key);
      return icon == null ? UIManager.getIcon("FileView.fileIcon") : icon;
   }
}
