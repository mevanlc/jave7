package net.disy.commons.swing.filechooser.result;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class FileChooserSaveResult extends AbstractFileChooserResult implements IFileChooserSaveResult {
   private final FileFilter selectedFilter;

   public FileChooserSaveResult(File selectedFile, FileFilter selectedFilter) {
      super(selectedFile);
      this.selectedFilter = selectedFilter;
   }

   @Override
   public FileFilter getSelectedFileFilter() {
      return this.selectedFilter;
   }
}
