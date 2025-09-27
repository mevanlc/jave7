package net.disy.commons.swing.dialog.userdialog.message;

import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.Ensure;

public class DialogMessageHandler implements IDialogMessageHandler {
   private final IBasicMessage defaultMessage;
   private final IErrorMessageProvider errorMessageProvider;

   public DialogMessageHandler(String defaultMessageText, IErrorMessageProvider errorMessageProvider) {
      Ensure.ensureArgumentNotNull("DefaultMessage must not be null.", defaultMessageText);
      Ensure.ensureArgumentNotNull("ErrorMessageHandler must not be null.", errorMessageProvider);
      this.defaultMessage = new BasicMessage(defaultMessageText);
      this.errorMessageProvider = errorMessageProvider;
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      IBasicMessage errorMessage = this.errorMessageProvider.getErrorMessage();
      return errorMessage != null ? errorMessage : this.getDefaultMessage();
   }

   @Override
   public IBasicMessage getDefaultMessage() {
      return this.defaultMessage;
   }
}
