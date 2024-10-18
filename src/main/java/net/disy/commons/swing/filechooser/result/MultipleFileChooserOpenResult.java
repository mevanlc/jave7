package net.disy.commons.swing.filechooser.result;

import java.io.File;
import net.disy.commons.core.util.Ensure;

public class MultipleFileChooserOpenResult implements IMultipleFileChooserOpenResult {
   private final File[] files;

   public MultipleFileChooserOpenResult(File[] files) {
      Ensure.ensureArgumentNotNull(files);
      this.files = files;
   }

   @Override
   public boolean isCanceled() {
      return this.files.length == 0;
   }

   @Override
   public File[] getSelectedFiles() {
      return this.files;
   }
}
