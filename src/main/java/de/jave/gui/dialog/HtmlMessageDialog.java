package de.jave.gui.dialog;

import net.disy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;

public class HtmlMessageDialog extends DefaultDialogConfiguration<HtmlMessagePage> {
   public HtmlMessageDialog(HtmlMessage message) {
      super(new HtmlMessagePage(message), DialogButtonConfigurationFactory.createOkOnly());
   }

   @Override
   public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
      return DialogHeaderPanelConfiguration.createInvisible();
   }
}
