package net.disy.commons.swing.dialog.input;

import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.HighestPriorityMessageBuilder;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;

public abstract class SmartDialogPage extends BasicSmartDialogPage implements IDialogPage {
   private final IBasicMessage defaultMessage;

   public SmartDialogPage(String defaultMessageText) {
      Ensure.ensureArgumentNotNull(defaultMessageText);
      this.defaultMessage = new BasicMessage(defaultMessageText);
   }

   @Override
   public IBasicMessage getDefaultMessage() {
      return this.defaultMessage;
   }

   @Override
   protected void addAdditionalMessages(HighestPriorityMessageBuilder builder) {
      builder.addMessage(this.getDefaultMessage());
   }
}
