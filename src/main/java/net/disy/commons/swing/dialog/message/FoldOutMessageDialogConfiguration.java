package net.disy.commons.swing.dialog.message;

import net.disy.commons.core.message.IMessage;
import net.disy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.foldout.AbstractFoldOutDialogConfiguration;
import net.disy.commons.swing.dialog.foldout.IFoldOutPage;
import net.disy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;

public class FoldOutMessageDialogConfiguration extends AbstractFoldOutDialogConfiguration<MessageDialogPage> {
   public FoldOutMessageDialogConfiguration(IMessage message, IFoldOutPage foldOutPage) {
      super(new MessageDialogPage(message), foldOutPage, DialogButtonConfigurationFactory.createOkOnly());
   }

   @Override
   public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
      return DialogHeaderPanelConfiguration.createInvisible();
   }
}
