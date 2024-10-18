package net.disy.commons.swing.dialog.core;

import net.disy.commons.swing.dialog.userdialog.IMessageSetable;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.events.IInputValidCheckable;

public class DialogPageInputValidCheckable implements IInputValidCheckable {
   private final IMessageSetable messageSetable;
   private final IDialogPage page;

   public DialogPageInputValidCheckable(IMessageSetable messageSetable, IDialogPage page) {
      this.messageSetable = messageSetable;
      this.page = page;
   }

   @Override
   public void checkInputValid() {
      this.messageSetable.setMessage(this.page.createCurrentMessage());
      this.page.updateInputValid();
   }
}
