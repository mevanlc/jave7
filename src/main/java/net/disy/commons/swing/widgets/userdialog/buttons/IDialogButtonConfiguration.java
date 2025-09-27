package net.disy.commons.swing.dialog.userdialog.buttons;

import net.disy.commons.swing.action.IActionConfiguration;

public interface IDialogButtonConfiguration {
   IActionConfiguration getOkActionConfiguration();

   IActionConfiguration getCancelActionConfiguration();
}
