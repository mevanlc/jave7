package net.disy.commons.swing.dialog.userdialog.message;

import net.disy.commons.core.message.IBasicMessage;

public interface IDialogMessageHandler {
   IBasicMessage createCurrentMessage();

   IBasicMessage getDefaultMessage();
}
