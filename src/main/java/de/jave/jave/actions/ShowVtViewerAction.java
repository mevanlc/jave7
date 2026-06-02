package de.jave.jave.actions;

import de.jave.gui.io.AcceptAllFileFilter;
import de.jave.gui.io.ExtensionFileFilters;
import de.jave.gui.io.FileChooserUtilities;
import de.jave.gui.io.FileSelection;
import de.jave.gui.io.IFileChooserConfiguration;
import de.jave.gui.io.SmartFileFilter;
import de.jave.jave.icon.JaveIcons;
import de.jave.vi.ViPlayMode;
import de.jave.vi.ViViewerApplication;
import java.awt.Component;
import java.io.File;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;

public class ShowVtViewerAction extends SmartAction {
   private final FileModel currentDirectoryModel;

   public ShowVtViewerAction(FileModel currentDirectoryModel) {
      super("Open VT Animation (experimental)...", JaveIcons.OPEN_ICON);
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      this.currentDirectoryModel = currentDirectoryModel;
   }

   @Override
   protected void execute(Component parentComponent) {
      IFileChooserConfiguration configuration = new IFileChooserConfiguration() {
         @Override
         public boolean isMultipleOpenFileSelectionAllowed() {
            return false;
         }

         @Override
         public String getSaveDialogTitle() {
            throw new UnsupportedOperationException();
         }

         @Override
         public String getOpenDialogTitle() {
            return "Open VT Animation file";
         }

         @Override
         public String getFileNameSuggestion() {
            return null;
         }

         @Override
         public SmartFileFilter[] getFileFilters() {
            return new SmartFileFilter[]{ExtensionFileFilters.VT, new AcceptAllFileFilter()};
         }

         @Override
         public FileModel getCurrentDirectoryModel() {
            return ShowVtViewerAction.this.currentDirectoryModel;
         }
      };
      FileSelection fileSelection = FileChooserUtilities.performOpenFileChooser(parentComponent, configuration);
      if (!fileSelection.isEmpty()) {
         File file = fileSelection.getFile();
         this.execute(parentComponent, file);
      }
   }

   public void execute(Component parentComponent, File file) {
      ViViewerApplication.playFile(parentComponent, file, ViPlayMode.LINE, 10);
   }
}
