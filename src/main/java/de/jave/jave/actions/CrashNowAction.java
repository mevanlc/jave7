package de.jave.jave.actions;

import de.jave.jave.JaveMessages;
import java.awt.Component;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.dialog.core.IDialogResult;
import net.dizzy.commons.swing.dialog.message.MessageUserDialogConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.UserDialog;
import net.dizzy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;

public class CrashNowAction extends SmartAction {
   public CrashNowAction() {
      super("Crash now");
   }

   @Override
   protected void execute(Component parentComponent) {
      MessageUserDialogConfiguration configuration = new MessageUserDialogConfiguration(
         new Message(JaveMessages.JavE, "Crashing is not fun!!\n\nDo you really want the program to crash now?", MessageType.WARNING),
         DialogButtonConfigurationFactory.createYesCancel()
      );
      UserDialog userDialog = new UserDialog(parentComponent, configuration);
      IDialogResult result = userDialog.show();
      if (!result.isCanceled()) {
         System.err.println("===============================================");
         System.err.println("By your personal demand:");
         System.err.println("JavE is now crashing w/o doing all the cleanup!");
         System.err.println(" - Good bye.");
         System.err.println("===============================================");
         System.exit(1);
      }
   }
}
