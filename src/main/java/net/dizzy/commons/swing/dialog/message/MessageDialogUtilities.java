package net.dizzy.commons.swing.dialog.message;

import java.awt.Component;

import javax.swing.JOptionPane;

import net.dizzy.commons.core.message.IBasicMessage;

public final class MessageDialogUtilities {
   private MessageDialogUtilities() {
   }

   public static YesNoCancel showYesNoCancelDialog(Component parent, IBasicMessage message) {
      int result = JOptionPane.showConfirmDialog(parent, message.getText(), message.getTitle(), JOptionPane.YES_NO_CANCEL_OPTION, MessageDialogFactory.optionType(message.getType()));
      if (result == JOptionPane.YES_OPTION) {
         return YesNoCancel.YES;
      }
      if (result == JOptionPane.NO_OPTION) {
         return YesNoCancel.NO;
      }
      return YesNoCancel.CANCEL;
   }

   public static boolean showOkCancelDialog(Component parent, IBasicMessage message) {
      return JOptionPane.showConfirmDialog(parent, message.getText(), message.getTitle(), JOptionPane.OK_CANCEL_OPTION, MessageDialogFactory.optionType(message.getType())) == JOptionPane.OK_OPTION;
   }

   public static boolean showYesNoDialog(Component parent, IBasicMessage message) {
      return JOptionPane.showConfirmDialog(parent, message.getText(), message.getTitle(), JOptionPane.YES_NO_OPTION, MessageDialogFactory.optionType(message.getType())) == JOptionPane.YES_OPTION;
   }
}
