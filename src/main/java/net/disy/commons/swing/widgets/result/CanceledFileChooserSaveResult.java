package net.disy.commons.swing.filechooser.result;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class CanceledFileChooserSaveResult implements IFileChooserSaveResult {
   @Override
   public File getSelectedFile() {
      throw new UnsupportedOperationException("FileChooser canceled");
   }

   @Override
   public boolean isCanceled() {
      return true;
   }

   @Override
   public FileFilter getSelectedFileFilter() {
      throw new UnsupportedOperationException("FileChooser canceled");
   }
}
