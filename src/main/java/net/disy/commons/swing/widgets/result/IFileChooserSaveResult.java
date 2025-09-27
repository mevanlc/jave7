package net.disy.commons.swing.filechooser.result;

import javax.swing.filechooser.FileFilter;

public interface IFileChooserSaveResult extends IFileChooserResult {
   FileFilter getSelectedFileFilter();
}
