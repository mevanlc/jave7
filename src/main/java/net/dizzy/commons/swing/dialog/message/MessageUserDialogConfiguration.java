package net.dizzy.commons.swing.dialog.message;

import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory.DialogButtonConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.page.AbstractDialogPage;

public class MessageUserDialogConfiguration extends DefaultDialogConfiguration<AbstractDialogPage> {
   public MessageUserDialogConfiguration(Message message, DialogButtonConfiguration buttonConfiguration) {
      super(new MessageDialogPage(message), buttonConfiguration);
   }

   private static class MessageDialogPage extends AbstractDialogPage {
      private final Message message;

      MessageDialogPage(Message message) {
         super(message.getText());
         this.message = message;
      }

      @Override protected javax.swing.JComponent createContent() { return new javax.swing.JLabel(message.getText()); }
      @Override public String getTitle() { return message.getTitle(); }
      @Override protected net.dizzy.commons.core.message.IBasicMessage createCurrentMessage() { return message; }
   }
}
