package net.disy.commons.swing.dialog.core;

import javax.swing.Icon;
import net.disy.commons.swing.dialog.core.preferences.IDialogPreferences;
import net.disy.commons.swing.dialog.userdialog.buttons.IDialogButtonConfiguration;

public interface IGenericDialogConfiguration {
   IDialogButtonConfiguration getButtonConfiguration();

   @Deprecated
   boolean isHeaderPanelVisible();

   @Deprecated
   Icon getLargeDialogIcon();

   IDialogHeaderPanelConfiguration getHeaderPanelConfiguration();

   IVetoDialogCloseHandler getVetoCloseHandler();

   IDialogPreferences getPreferences();
}
