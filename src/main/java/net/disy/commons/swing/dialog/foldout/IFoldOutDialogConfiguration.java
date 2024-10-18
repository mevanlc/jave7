package net.disy.commons.swing.dialog.foldout;

import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.dialog.userdialog.IDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;

public interface IFoldOutDialogConfiguration<P extends IDialogPage> extends IDialogConfiguration<P> {
   IFoldOutPage getFoldOutPage();

   IActionConfiguration getFoldOutButtonConfiguration();

   IActionConfiguration getFoldInButtonConfiguration();

   boolean isInitiallyFoldedOut();
}
