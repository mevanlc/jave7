package net.disy.commons.swing.dialog.action;

import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;

public interface IDialogInput {
   IDialogPage createPage();

   void confirm();
}
