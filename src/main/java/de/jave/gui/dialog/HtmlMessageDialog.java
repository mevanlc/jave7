package de.jave.gui.dialog;

import net.dizzy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;

public class HtmlMessageDialog extends DefaultDialogConfiguration<HtmlMessagePage> {
   public HtmlMessageDialog(HtmlMessage message) {
      super(new HtmlMessagePage(message), DialogButtonConfigurationFactory.createOkOnly());
   }

   @Override
   public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
      return DialogHeaderPanelConfiguration.createInvisible();
   }
}
