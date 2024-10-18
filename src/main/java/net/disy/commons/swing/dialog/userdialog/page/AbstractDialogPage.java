package net.disy.commons.swing.dialog.userdialog.page;

import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.Ensure;

public abstract class AbstractDialogPage extends AbstractBasicDialogPage implements IDialogPage {
   private final IBasicMessage defaultMessage;

   public AbstractDialogPage(String defaultMessageText) {
      Ensure.ensureArgumentNotNull("DefaultMessage text must not be null.", defaultMessageText);
      this.defaultMessage = new BasicMessage(defaultMessageText);
   }

   @Override
   public final IBasicMessage getDefaultMessage() {
      return this.defaultMessage;
   }

   @Override
   public abstract IBasicMessage createCurrentMessage();

   @Override
   public boolean canFinish() {
      return !this.createCurrentMessage().isErrorMessage();
   }
}
