package net.disy.commons.swing.filechooser.result;

import java.io.File;

public interface IFileChooserResult {
   boolean isCanceled();

   File getSelectedFile();
}
