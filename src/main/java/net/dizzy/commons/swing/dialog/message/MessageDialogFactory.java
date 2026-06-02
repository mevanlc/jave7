package net.dizzy.commons.swing.dialog.message;

import java.awt.Component;

import javax.swing.JOptionPane;

import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.swing.dialog.userdialog.UserDialog;
import net.dizzy.commons.swing.dialog.message.MessageUserDialogConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;

public final class MessageDialogFactory {
   private MessageDialogFactory() {
   }

   public static void showMessageDialog(Component parent, IBasicMessage message) {
      JOptionPane.showMessageDialog(parent, message.getText(), title(message), optionType(message.getType()));
   }

   public static UserDialog createMessageDialog(Component parent, IBasicMessage message) {
      net.dizzy.commons.core.message.Message fullMessage = message instanceof net.dizzy.commons.core.message.Message
         ? (net.dizzy.commons.core.message.Message) message
         : new net.dizzy.commons.core.message.Message(message.getTitle(), message.getText(), message.getType());
      return new UserDialog(parent, new MessageUserDialogConfiguration(fullMessage, DialogButtonConfigurationFactory.createCloseOnly()));
   }

   private static String title(IBasicMessage message) {
      return message.getTitle() == null ? "" : message.getTitle();
   }

   static int optionType(MessageType type) {
      if (type == MessageType.ERROR) {
         return JOptionPane.ERROR_MESSAGE;
      }
      if (type == MessageType.WARNING) {
         return JOptionPane.WARNING_MESSAGE;
      }
      if (type == MessageType.QUESTION) {
         return JOptionPane.QUESTION_MESSAGE;
      }
      if (type == MessageType.INFORMATION) {
         return JOptionPane.INFORMATION_MESSAGE;
      }
      return JOptionPane.PLAIN_MESSAGE;
   }
}
