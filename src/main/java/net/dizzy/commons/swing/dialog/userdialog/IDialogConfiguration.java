package net.dizzy.commons.swing.dialog.userdialog;

import java.awt.Component;

import javax.swing.JComponent;

import net.dizzy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory.DialogButtonConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.page.IDialogPage;

public interface IDialogConfiguration<P extends IDialogPage> {
   P getPage();

   DialogButtonConfiguration getButtonConfiguration();

   IDialogHeaderPanelConfiguration getHeaderPanelConfiguration();

   boolean performOk(Component parent);

   boolean performCancel(Component parent);

   JComponent[] createAdditionalButtons();
}
