package net.disy.commons.swing.filechooser.result;

import java.io.File;

public abstract class AbstractFileChooserResult {
   private final File selectedFile;

   public AbstractFileChooserResult(File selectedFile) {
      this.selectedFile = selectedFile;
   }

   public final boolean isCanceled() {
      return this.selectedFile == null;
   }

   public final File getSelectedFile() {
      return this.selectedFile;
   }
}
