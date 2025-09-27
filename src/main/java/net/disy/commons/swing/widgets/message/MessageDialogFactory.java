package net.disy.commons.swing.dialog.message;

import java.awt.Component;
import net.disy.commons.core.message.IMessage;
import net.disy.commons.swing.dialog.foldout.FoldOutDialog;
import net.disy.commons.swing.dialog.foldout.IFoldOutPage;
import net.disy.commons.swing.dialog.message.internal.MessageDetailsFoldOutPage;
import net.disy.commons.swing.dialog.userdialog.UserDialog;

public class MessageDialogFactory {
   public static FoldOutDialog createFoldOutMessageDialog(Component parentComponent, IMessage message, IFoldOutPage foldOutPage) {
      return new FoldOutDialog(parentComponent, new FoldOutMessageDialogConfiguration(message, foldOutPage));
   }

   public static UserDialog createMessageDialog(Component parentComponent, IMessage message) {
      UserDialog userDialog;
      if (message.getDetail() == null) {
         userDialog = new UserDialog(parentComponent, new MessageUserDialogConfiguration(message));
         userDialog.getDialog().setResizable(false);
      } else {
         IFoldOutPage foldOutPage = new MessageDetailsFoldOutPage(message.getDetail());
         FoldOutMessageDialogConfiguration dialogConfiguration = new FoldOutMessageDialogConfiguration(message, foldOutPage);
         userDialog = new FoldOutDialog(parentComponent, dialogConfiguration);
      }

      return userDialog;
   }

   public static void showMessageDialog(Component parent, IMessage message) {
      createMessageDialog(parent, message).show();
   }
}
