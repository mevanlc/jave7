package net.disy.commons.swing.filechooser.result;

import java.io.File;

public interface IMultipleFileChooserOpenResult {
   boolean isCanceled();

   File[] getSelectedFiles();
}
