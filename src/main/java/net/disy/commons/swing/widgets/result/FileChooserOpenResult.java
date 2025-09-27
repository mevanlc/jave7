package net.disy.commons.swing.filechooser.result;

import java.io.File;

public class FileChooserOpenResult extends AbstractFileChooserResult implements IFileChooserOpenResult {
   public FileChooserOpenResult(File selectedFile) {
      super(selectedFile);
   }
}
