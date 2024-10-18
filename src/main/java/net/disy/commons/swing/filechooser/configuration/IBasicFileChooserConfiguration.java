package net.disy.commons.swing.filechooser.configuration;

import javax.swing.filechooser.FileFilter;
import net.disy.commons.swing.filechooser.chooser.IFileChooser;

public interface IBasicFileChooserConfiguration {
   boolean acceptAllFileFilter();

   void setAccessory(IFileChooser var1);

   FileFilter[] getChoosableFileFilters();

   FileFilter getDefaultFileFilter();

   String getTitle();
}
