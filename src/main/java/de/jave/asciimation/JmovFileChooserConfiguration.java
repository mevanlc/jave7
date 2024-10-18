package de.jave.asciimation;

import de.jave.gui.io.ExtensionFileFilters;
import de.jave.gui.io.IFileChooserConfiguration;
import de.jave.gui.io.SmartFileFilter;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.util.Ensure;

public class JmovFileChooserConfiguration implements IFileChooserConfiguration {
   private final FileModel currentDirectoryModel;

   public JmovFileChooserConfiguration(FileModel currentDirectoryModel) {
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      this.currentDirectoryModel = currentDirectoryModel;
   }

   @Override
   public FileModel getCurrentDirectoryModel() {
      return this.currentDirectoryModel;
   }

   @Override
   public String getOpenDialogTitle() {
      return "Open JavE Animation";
   }

   @Override
   public String getSaveDialogTitle() {
      return "Save JavE Animation";
   }

   @Override
   public SmartFileFilter[] getFileFilters() {
      return new SmartFileFilter[]{ExtensionFileFilters.JMOV};
   }

   @Override
   public String getFileNameSuggestion() {
      return "*.jmov";
   }

   @Override
   public boolean isMultipleOpenFileSelectionAllowed() {
      return false;
   }
}
