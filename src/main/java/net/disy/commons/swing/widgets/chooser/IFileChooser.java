package net.disy.commons.swing.filechooser.chooser;

import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.event.AncestorListener;
import javax.swing.filechooser.FileFilter;

public interface IFileChooser {
   String SELECTED_FILE_CHANGED_PROPERTY_NAME = "SelectedFileChangedProperty";

   void setFileSelectionMode(int var1);

   void setFileFilter(FileFilter var1);

   void setAccessory(JComponent var1);

   void setMinimumSize(Dimension var1);

   void setPreferredSize(Dimension var1);

   File getCurrentDirectory();

   void setCurrentDirectory(File var1);

   void setDialogTitle(String var1);

   void setAcceptAllFileFilterUsed(boolean var1);

   String getSuggestedFileName();

   void setSuggestedFileName(String var1);

   File getSelectedFile();

   void setSelectedFile(File var1);

   int showOpenDialog(Component var1);

   int showSaveDialog(Component var1);

   void resetChoosableFileFilters();

   void addChoosableFileFilter(FileFilter var1);

   String getUiDialogTitle();

   Icon getSystemIcon(File var1);

   JComponent getContent();

   FileFilter getFileFilter();

   void addPropertyChangeListener(PropertyChangeListener var1);

   void removePropertyChangeListener(PropertyChangeListener var1);

   void addAncestorListener(AncestorListener var1);

   void requestFocus();

   FileFilter getAcceptAllFileFilter();

   void setMultipleSelectionEnabled(boolean var1);

   File[] getSelectedFiles();
}
