package net.dizzy.commons.swing.dialog.userdialog.page;

import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.swing.dialog.core.IDialogHelpHandler;

public interface IDialogPage {
   String getTitle();

   IBasicMessage getDefaultMessage();

   void checkInputValid();

   IDialogHelpHandler getHelpHandler();
}
