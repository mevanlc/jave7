package net.disy.commons.swing.dialog.userdialog;

import net.disy.commons.swing.dialog.core.IDialogContainer;

public interface IUserDialogContainer extends IDialogContainer {
   void showNonModal();

   void showNonModal(IDialogCloseHandler var1);

   void setVisible(boolean var1);
}
