package net.dizzy.commons.swing.filechooser.util;

import java.io.File;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import net.dizzy.commons.swing.ui.IObjectUi;

public class FileObjectUi implements IObjectUi<File> {
   private final FileSystemView view = FileSystemView.getFileSystemView();

   public FileObjectUi() {
   }

   public FileObjectUi(FileSystemView view) {
      // Keep the constructor shape; the shared view is sufficient here.
   }

   @Override public String getLabel(File object) { return view.getSystemDisplayName(object); }
   @Override public Icon getIcon(File object) { return view.getSystemIcon(object); }
}
