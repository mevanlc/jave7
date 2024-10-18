package net.disy.commons.swing.dialog.message;

import net.disy.commons.core.message.IMessage;
import net.disy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;
import net.disy.commons.swing.dialog.userdialog.buttons.IDialogButtonConfiguration;

public class MessageUserDialogConfiguration extends DefaultDialogConfiguration<MessageDialogPage> {
   public MessageUserDialogConfiguration(IMessage message) {
      this(message, DialogButtonConfigurationFactory.createOkOnly());
   }

   public MessageUserDialogConfiguration(IMessage message, IDialogButtonConfiguration buttonConfiguration) {
      super(new MessageDialogPage(message), buttonConfiguration);
   }

   @Override
   public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
      return DialogHeaderPanelConfiguration.createInvisible();
   }
}
