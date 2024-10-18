package net.disy.commons.swing.filechooser.dialog;

import java.io.File;
import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.util.StringUtilities;
import net.disy.commons.swing.dialog.input.text.ITextInputDialogConfiguration;

public class NewFolderInputDialogConfiguration implements ITextInputDialogConfiguration {
   private static final BasicMessage NAME_EXISTS_MESSAGE = new BasicMessage(
      "Es existiert bereits ein Ordner mit diesem Namen. Bitte geben Sie einen anderen Namen ein.", MessageType.ERROR
   );
   private static final BasicMessage NO_NAME_SELECTED_MESSAGE = new BasicMessage(
      "Der Ordner-Name ist leer. Bitte geben Sie den Namen des Ordners ein.", MessageType.ERROR
   );
   private final File folder;

   public NewFolderInputDialogConfiguration(File folder) {
      this.folder = folder;
   }

   @Override
   public String getTitle() {
      return "Neuer Ordner";
   }

   @Override
   public String getDefaultMessageText() {
      return "Bitte geben Sie den Namen des neuen Ordners ein.";
   }

   @Override
   public String getLabelText() {
      return "Name des Ordners";
   }

   @Override
   public IBasicMessage createCurrentMessage(String selectedText) {
      String fileName = selectedText.trim();
      if (StringUtilities.isNullOrEmpty(fileName)) {
         return NO_NAME_SELECTED_MESSAGE;
      } else {
         return new File(this.folder, fileName).exists() ? NAME_EXISTS_MESSAGE : null;
      }
   }
}
