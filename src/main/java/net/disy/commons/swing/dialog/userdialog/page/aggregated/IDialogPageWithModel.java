package net.disy.commons.swing.dialog.userdialog.page.aggregated;

import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;

public interface IDialogPageWithModel<M> {
   IDialogPage getDialogPage();

   M getModel();

   String getLabel();
}
