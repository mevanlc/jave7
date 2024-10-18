package net.disy.commons.swing.filechooser;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.filechooser.FileFilter;
import net.disy.commons.core.message.IMessage;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;
import net.disy.commons.swing.dialog.message.MessageUserDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;
import net.disy.commons.swing.dialog.userdialog.buttons.IDialogButtonConfiguration;
import net.disy.commons.swing.filechooser.chooser.IFileChooser;
import net.disy.commons.swing.filechooser.configuration.IBasicFileChooserConfiguration;
import net.disy.commons.swing.filechooser.configuration.IFileChooserOpenConfiguration;
import net.disy.commons.swing.filechooser.configuration.IFileChooserSaveConfiguration;
import net.disy.commons.swing.filechooser.dialog.FileChooserUtilities;
import net.disy.commons.swing.filechooser.result.CanceledFileChooserSaveResult;
import net.disy.commons.swing.filechooser.result.FileChooserOpenResult;
import net.disy.commons.swing.filechooser.result.FileChooserSaveResult;
import net.disy.commons.swing.filechooser.result.IFileChooserOpenResult;
import net.disy.commons.swing.filechooser.result.IFileChooserSaveResult;
import net.disy.commons.swing.filechooser.result.IMultipleFileChooserOpenResult;
import net.disy.commons.swing.filechooser.result.MultipleFileChooserOpenResult;
import net.disy.commons.swing.filechooser.workingdirectory.IWorkingDirectoryPreference;
import net.disy.commons.swing.filechooser.workingdirectory.TransientWorkingDirectoryPreference;

public class SmartFileChooser {
   private static final SmartFileChooser instance = new SmartFileChooser();
   private IWorkingDirectoryPreference preference = new TransientWorkingDirectoryPreference();
   private IFileChooserProvider fileChooserProvider;

   public static SmartFileChooser getInstance() {
      return instance;
   }

   private SmartFileChooser() {
   }

   public void setWorkingDirectoryPreference(IWorkingDirectoryPreference preference) {
      Ensure.ensureArgumentNotNull(preference);
      this.preference = preference;
   }

   public void setFileChooserProvider(IFileChooserProvider fileChooserProvider) {
      Ensure.ensureArgumentNotNull(fileChooserProvider);
      this.fileChooserProvider = fileChooserProvider;
   }

   public IFileChooserOpenResult performOpenFileChooser(Component parent, IFileChooserOpenConfiguration configuration) {
      return this.performOpenFileChooser(parent, configuration, null);
   }

   public IFileChooserOpenResult performOpenFileChooser(Component parent, IFileChooserOpenConfiguration configuration, File file) {
      Ensure.ensureNotNull("SmartFileChoosing must be initialized with FileChooserProvider.", this.fileChooserProvider);
      IFileChooser fileChooser = this.initFileChooser(this.fileChooserProvider.getFileChooser(), configuration);
      fileChooser.setFileSelectionMode(configuration.getFileSelectionMode());
      fileChooser.setMultipleSelectionEnabled(false);
      if (file != null) {
         fileChooser.setSelectedFile(file);
      }

      while (true) {
         int approveOption = fileChooser.showOpenDialog(parent);
         if (approveOption != 0) {
            return new FileChooserOpenResult(null);
         }

         this.preference.setWorkingDirectory(fileChooser.getCurrentDirectory());
         File selectedFile = fileChooser.getSelectedFile();
         if (selectedFile.exists()) {
            return new FileChooserOpenResult(selectedFile);
         }

         MessageDialogFactory.showMessageDialog(
            parent,
            new Message("Die gewÃ¤hlte Datei '" + selectedFile.getName() + "' existiert nicht. WÃ¤hlen Sie bitte eine vorhandene Datei.", MessageType.WARNING)
         );
      }
   }

   public IMultipleFileChooserOpenResult performMultipleOpenFileChooser(Component parent, IFileChooserOpenConfiguration configuration) {
      Ensure.ensureNotNull("SmartFileChoosing must be initialized with FileChooserProvider.", this.fileChooserProvider);
      IFileChooser fileChooser = this.initFileChooser(this.fileChooserProvider.getFileChooser(), configuration);
      fileChooser.setFileSelectionMode(configuration.getFileSelectionMode());
      fileChooser.setMultipleSelectionEnabled(true);

      while (true) {
         int approveOption = fileChooser.showOpenDialog(parent);
         if (approveOption != 0) {
            return new MultipleFileChooserOpenResult(new File[0]);
         }

         this.preference.setWorkingDirectory(fileChooser.getCurrentDirectory());
         File[] selectedFiles = fileChooser.getSelectedFiles();
         List<String> nonExistingFiles = new ArrayList<>();

         for (File file : selectedFiles) {
            if (!file.exists()) {
               nonExistingFiles.add(file.getName());
            }
         }

         if (nonExistingFiles.size() == 0) {
            return new MultipleFileChooserOpenResult(selectedFiles);
         }

         if (nonExistingFiles.size() == 1) {
            MessageDialogFactory.showMessageDialog(
               parent,
               new Message(
                  "Die gewÃ¤hlte Datei '" + selectedFiles[0].getName() + "' existiert nicht. WÃ¤hlen Sie bitte eine vorhandene Datei.", MessageType.WARNING
               )
            );
         } else if (nonExistingFiles.size() > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Die gewÃ¤hlten Dateien '");
            boolean firstFile = true;

            for (String fileName : nonExistingFiles) {
               if (!firstFile) {
                  stringBuilder.append(", ");
                  firstFile = false;
               }

               stringBuilder.append(fileName);
            }

            stringBuilder.append("' existieren nicht. WÃ¤hlen Sie bitte vorhandene Dateien.");
            MessageDialogFactory.showMessageDialog(parent, new Message(stringBuilder.toString(), MessageType.WARNING));
         }
      }
   }

   public IFileChooserSaveResult performSaveFileChooser(Component parent, IFileChooserSaveConfiguration configuration, File file, String suggestedFileName) {
      Ensure.ensureArgumentNotNull("A configuration must be specified", configuration);
      Ensure.ensureNotNull("SmartFileChoosing must be initialized with FileChooserProvider.", this.fileChooserProvider);
      IFileChooser fileChooser = this.initFileChooser(this.fileChooserProvider.getFileChooser(), configuration);
      fileChooser.setMultipleSelectionEnabled(false);
      if (file != null) {
         fileChooser.setCurrentDirectory(file.getParentFile());
         fileChooser.setSuggestedFileName(file.getName());
      } else {
         fileChooser.setSuggestedFileName(suggestedFileName);
      }

      int approveOption = fileChooser.showSaveDialog(parent);
      if (approveOption != 0) {
         return new CanceledFileChooserSaveResult();
      } else {
         this.preference.setWorkingDirectory(fileChooser.getCurrentDirectory());
         File selectedFile = FileChooserUtilities.getFileWithExtension(
            fileChooser.getSelectedFile(), fileChooser.getFileFilter(), fileChooser.getAcceptAllFileFilter(), configuration
         );
         if (selectedFile.exists()) {
            String messageText = Messages.getString("SmartFileChooser.OverwriteDialog.Message", new Object[]{selectedFile.getName()});
            IMessage confirmMessage = new Message(messageText, MessageType.WARNING);
            IDialogButtonConfiguration buttonConfiguration = DialogButtonConfigurationFactory.createYesNo();
            UserDialog confirmDialog = new UserDialog(parent, new MessageUserDialogConfiguration(confirmMessage, buttonConfiguration));
            IDialogResult result = confirmDialog.show();
            if (result.isCanceled()) {
               return new CanceledFileChooserSaveResult();
            }
         }

         return new FileChooserSaveResult(selectedFile, fileChooser.getFileFilter());
      }
   }

   private IFileChooser initFileChooser(IFileChooser fileChooser, IBasicFileChooserConfiguration configuration) {
      if (configuration.getTitle() != null) {
         fileChooser.setDialogTitle(configuration.getTitle());
      }

      this.setFileFilters(fileChooser, configuration);
      this.applyWorkingDirectory(fileChooser);
      configuration.setAccessory(fileChooser);
      return fileChooser;
   }

   private void setFileFilters(IFileChooser fileChooser, IBasicFileChooserConfiguration configuration) {
      fileChooser.setAcceptAllFileFilterUsed(configuration.acceptAllFileFilter());
      fileChooser.resetChoosableFileFilters();
      FileFilter[] choosableFilters = configuration.getChoosableFileFilters();

      for (int index = 0; index < choosableFilters.length; index++) {
         fileChooser.addChoosableFileFilter(choosableFilters[index]);
      }

      FileFilter defaultFileFilter = configuration.getDefaultFileFilter();
      if (defaultFileFilter != null) {
         fileChooser.setFileFilter(defaultFileFilter);
      }
   }

   private void applyWorkingDirectory(IFileChooser fileChooser) {
      File workingDirectory = this.preference.getWorkingDirectory();
      if (workingDirectory != null) {
         fileChooser.setCurrentDirectory(workingDirectory);
      }
   }
}
