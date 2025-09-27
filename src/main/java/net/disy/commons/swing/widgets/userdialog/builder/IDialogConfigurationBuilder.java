package net.disy.commons.swing.dialog.userdialog.builder;

import net.disy.commons.swing.dialog.userdialog.IDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.buttons.IDialogButtonConfiguration;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;

public interface IDialogConfigurationBuilder {
   <P extends IDialogPage> IDialogConfiguration<P> create(P var1);

   <P extends IDialogPage> IDialogConfiguration<P> create(P var1, IDialogButtonConfiguration var2);

   IDialogConfigurationBuilder invisibleHeaderPanel();
}
