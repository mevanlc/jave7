package de.jave.gui.io;

import java.awt.Component;
import java.io.File;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.filechooser.FileSystemView;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.filechooser.util.FileObjectUi;

public class FileListCellRenderer extends DefaultListCellRenderer {
   private final FileObjectUi fileObjectUi;

   public FileListCellRenderer(FileSystemView fileSystemView) {
      Ensure.ensureArgumentNotNull(fileSystemView);
      this.fileObjectUi = new FileObjectUi(fileSystemView);
   }

   @Override
   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      if (value instanceof File) {
         File file = (File)value;
         this.setText(this.fileObjectUi.getLabel(file));
         this.setIcon(this.fileObjectUi.getIcon(file));
      }

      return this;
   }
}
