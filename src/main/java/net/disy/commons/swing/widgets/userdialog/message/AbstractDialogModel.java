package net.disy.commons.swing.dialog.userdialog.message;

import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;

public abstract class AbstractDialogModel implements IErrorMessageProvider {
   @Override
   public final IBasicMessage getErrorMessage() {
      String errorMessageText = this.getErrorMessageText();
      return errorMessageText != null ? new BasicMessage(errorMessageText, MessageType.ERROR) : null;
   }

   protected String getErrorMessageText() {
      return null;
   }
}
