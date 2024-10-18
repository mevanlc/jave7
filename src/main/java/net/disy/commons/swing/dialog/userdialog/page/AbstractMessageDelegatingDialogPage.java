package net.disy.commons.swing.dialog.userdialog.page;

import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.userdialog.message.DialogMessageHandler;
import net.disy.commons.swing.dialog.userdialog.message.IDialogMessageHandler;
import net.disy.commons.swing.dialog.userdialog.message.IErrorMessageProvider;

public abstract class AbstractMessageDelegatingDialogPage extends AbstractBasicDialogPage implements IDialogPage {
   private final IDialogMessageHandler messageHandler;

   protected AbstractMessageDelegatingDialogPage(String defaultMessageText, IErrorMessageProvider errorMessageProvider) {
      this(new DialogMessageHandler(defaultMessageText, errorMessageProvider));
   }

   protected AbstractMessageDelegatingDialogPage(IDialogMessageHandler messageHandler) {
      Ensure.ensureArgumentNotNull("MessageHandler must not be null.", messageHandler);
      this.messageHandler = messageHandler;
   }

   @Override
   public final IBasicMessage getDefaultMessage() {
      return this.messageHandler.getDefaultMessage();
   }

   @Override
   public final IBasicMessage createCurrentMessage() {
      return this.messageHandler.createCurrentMessage();
   }
}
