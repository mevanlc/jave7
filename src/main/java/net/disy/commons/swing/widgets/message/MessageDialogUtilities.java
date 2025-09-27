package net.disy.commons.swing.dialog.message;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JComponent;
import net.disy.commons.core.message.IMessage;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogMessages;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;
import net.disy.commons.swing.dialog.userdialog.buttons.IDialogButtonConfiguration;

public class MessageDialogUtilities {
   public static boolean showDialog(Component parent, IMessage message, IDialogButtonConfiguration buttonConfiguration) {
      MessageUserDialogConfiguration configuration = new MessageUserDialogConfiguration(message, buttonConfiguration);
      UserDialog userDialog = new UserDialog(parent, configuration);
      IDialogResult result = userDialog.show();
      return !result.isCanceled();
   }

   public static YesNoCancel showYesNoCancelDialog(Component parent, IMessage message) {
      final BooleanModel noPressedModel = new BooleanModel(false);
      IDialogButtonConfiguration buttonConfiguration = DialogButtonConfigurationFactory.createYesCancel();
      MessageUserDialogConfiguration configuration = new MessageUserDialogConfiguration(message, buttonConfiguration) {
         @Override
         public JComponent[] createAdditionalButtons() {
            return new JComponent[]{new JButton(new SmartAction(DisyCommonsSwingDialogMessages.NO) {
               @Override
               protected void execute(Component parentComponent) {
                  noPressedModel.setValue(true);
                  getDialogContainer().requestFinish();
               }
            })};
         }
      };
      UserDialog userDialog = new UserDialog(parent, configuration);
      IDialogResult result = userDialog.show();
      if (result.isCanceled()) {
         return YesNoCancel.CANCEL;
      } else {
         return noPressedModel.getValue() ? YesNoCancel.NO : YesNoCancel.YES;
      }
   }

   public static boolean confirmUserOperation(Component parent, String message, String title) {
      return showYesNoDialog(parent, new Message(title, message, MessageType.QUESTION));
   }

   public static boolean confirmUserOperation(Component parent, String message) {
      return showYesNoDialog(parent, new Message(message, MessageType.QUESTION));
   }

   public static boolean showYesNoDialog(Component parent, IMessage message) {
      return showDialog(parent, message, DialogButtonConfigurationFactory.createYesNo());
   }

   public static boolean showOkCancelDialog(Component parent, Message message) {
      return showDialog(parent, message, DialogButtonConfigurationFactory.createOkCancel());
   }
}
