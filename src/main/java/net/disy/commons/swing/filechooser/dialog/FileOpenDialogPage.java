package net.disy.commons.swing.filechooser.dialog;

import java.io.File;
import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.swing.filechooser.model.FileChooserModel;

public class FileOpenDialogPage extends AbstractFileChooserDialogPage {
   private static final String DEFAULT_MESSAGE = "Bitte wÃ¤hlen Sie eine Datei zum Ã–ffnen aus.";
   private static final IBasicMessage NO_FILE_SELECTED_MESSAGE = new BasicMessage(
      "Es ist keine Datei ausgewÃ¤hlt. Bitte wÃ¤hlen Sie eine Datei aus.", MessageType.ERROR
   );
   private static final IBasicMessage DIRECTORY_SELECTED_MESSAGE = new BasicMessage(
      "Es ist ein Verzeichnis ausgewÃ¤hlt. Bitte wÃ¤hlen Sie eine einfache Datei aus.", MessageType.ERROR
   );
   private static final IBasicMessage FILE_NOT_EXISTENT_MESSAGE = new BasicMessage(
      "Die Datei existiert nicht. Bitte wÃ¤hlen Sie eine vorhandene Datei aus.", MessageType.ERROR
   );

   public FileOpenDialogPage(FileChooserModel model, FileChooserDialogConfiguration configuration) {
      super(model, configuration, "Bitte wÃ¤hlen Sie eine Datei zum Ã–ffnen aus.");
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      File file = this.getFileModel().getValue();
      if (file == null) {
         return NO_FILE_SELECTED_MESSAGE;
      } else if (file.isDirectory()) {
         return DIRECTORY_SELECTED_MESSAGE;
      } else {
         return !file.exists() ? FILE_NOT_EXISTENT_MESSAGE : this.getDefaultMessage();
      }
   }

   @Override
   public String getTitle() {
      return "Datei Ã¶ffnen";
   }
}
