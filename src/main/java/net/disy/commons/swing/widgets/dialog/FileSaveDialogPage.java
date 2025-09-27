package net.disy.commons.swing.filechooser.dialog;

import java.io.File;
import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.swing.filechooser.model.FileChooserModel;

public class FileSaveDialogPage extends AbstractFileChooserDialogPage {
   private static final String DEFAULT_MESSAGE = "Bitte wÃ¤hlen Sie einen Dateinamen zum Speichern aus.";
   private static final IBasicMessage NO_FILE_SELECTED_MESSAGE = new BasicMessage(
      "Es ist kein Dateiname ausgewÃ¤hlt. Bitte wÃ¤hlen Sie einen Dateinamen aus.", MessageType.ERROR
   );
   private static final IBasicMessage DIRECTORY_SELECTED_MESSAGE = new BasicMessage(
      "Es ist ein Verzeichnis ausgewÃ¤hlt. Bitte wÃ¤hlen Sie einen anderen Namen aus.", MessageType.ERROR
   );

   public FileSaveDialogPage(FileChooserModel model, FileChooserDialogConfiguration configuration) {
      super(model, configuration, "Bitte wÃ¤hlen Sie einen Dateinamen zum Speichern aus.");
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      File file = this.getFileModel().getValue();
      if (file == null) {
         return NO_FILE_SELECTED_MESSAGE;
      } else {
         return file.isDirectory() ? DIRECTORY_SELECTED_MESSAGE : this.getDefaultMessage();
      }
   }

   @Override
   public String getTitle() {
      return "Datei speichern";
   }
}
