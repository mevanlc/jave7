package de.jave.gui.io;

import net.dizzy.commons.core.io.FileModel;

public interface IFileChooserConfiguration {
   FileModel getCurrentDirectoryModel();

   String getSaveDialogTitle();

   String getOpenDialogTitle();

   SmartFileFilter[] getFileFilters();

   String getFileNameSuggestion();

   boolean isMultipleOpenFileSelectionAllowed();
}
