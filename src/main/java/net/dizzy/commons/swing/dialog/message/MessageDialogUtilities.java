package net.dizzy.commons.swing.dialog.message;

import java.awt.Component;

import javax.swing.JOptionPane;

import net.dizzy.commons.core.message.IBasicMessage;

public final class MessageDialogUtilities {
   private static final String SAVE_OPTION = "Save";
   private static final String DISCARD_OPTION = "Don't Save";
   private static final String CANCEL_OPTION = "Cancel";

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

   public static YesNoCancel showSaveDiscardCancelDialog(Component parent, IBasicMessage message) {
      Object[] options = {SAVE_OPTION, DISCARD_OPTION, CANCEL_OPTION};
      int result = JOptionPane.showOptionDialog(
         parent,
         message.getText(),
         message.getTitle(),
         JOptionPane.YES_NO_CANCEL_OPTION,
         MessageDialogFactory.optionType(message.getType()),
         null,
         options,
         SAVE_OPTION
      );
      return saveDiscardCancelResult(result);
   }

   static YesNoCancel saveDiscardCancelResult(int result) {
      if (result == 0) {
         return YesNoCancel.YES;
      }
      if (result == 1) {
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
