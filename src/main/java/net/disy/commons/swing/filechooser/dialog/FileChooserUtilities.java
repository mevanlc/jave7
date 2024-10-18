package net.disy.commons.swing.filechooser.dialog;

import java.awt.Component;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import net.disy.commons.core.io.FileUtilities;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.filechooser.configuration.IFileChooserSaveConfiguration;
import net.disy.commons.swing.filechooser.filefilter.ExtensionFileFilter;
import net.disy.commons.swing.filechooser.model.FileChooserModel;

public class FileChooserUtilities {
   private FileChooserUtilities() {
      throw new UnsupportedOperationException();
   }

   public static UserDialog createOpenFileDialog(Component parent, FileChooserModel model) {
      return new UserDialog(parent, new FileOpenDialogPage(model, new FileChooserDialogConfiguration()));
   }

   public static UserDialog createOpenFileDialog(Component parent, FileChooserModel model, FileChooserDialogConfiguration configuration) {
      return new UserDialog(parent, new FileOpenDialogPage(model, configuration));
   }

   public static UserDialog createSaveFileDialog(Component parent, FileChooserModel model) {
      return new UserDialog(parent, new FileSaveDialogPage(model, new FileChooserDialogConfiguration()));
   }

   public static UserDialog createSaveFileDialog(Component parent, FileChooserModel model, FileChooserDialogConfiguration configuration) {
      return new UserDialog(parent, new FileSaveDialogPage(model, configuration));
   }

   public static File getFileWithExtension(
      File selectedFile, FileFilter filter, FileFilter acceptAllFileFilter, IFileChooserSaveConfiguration fileChooserConfiguration
   ) {
      if (selectedFile == null) {
         return selectedFile;
      } else {
         if (filter == acceptAllFileFilter) {
            FileFilter[] choosableFileFilters = fileChooserConfiguration.getChoosableFileFilters();

            for (int index = 0; index < choosableFileFilters.length; index++) {
               if (choosableFileFilters[index].accept(selectedFile)) {
                  return selectedFile;
               }
            }
         } else if (filter.accept(selectedFile)) {
            return selectedFile;
         }

         String extension = filter instanceof ExtensionFileFilter
            ? ((ExtensionFileFilter)filter).getDefaultExtension()
            : fileChooserConfiguration.getDefaultExtension();
         return FileUtilities.addExtension(selectedFile, extension);
      }
   }
}
