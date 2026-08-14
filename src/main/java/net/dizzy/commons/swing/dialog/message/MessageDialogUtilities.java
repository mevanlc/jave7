package net.dizzy.commons.swing.dialog.message;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import net.dizzy.commons.core.message.IBasicMessage;

public final class MessageDialogUtilities {
   private static final String SAVE_OPTION = "Save";
   private static final String DISCARD_OPTION = "Don't Save";
   private static final String CANCEL_OPTION = "Cancel";
   private static final String DISCARD_ACTION = "messageDialog.discardChanges";

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
      JOptionPane optionPane = new JOptionPane(
         message.getText(),
         MessageDialogFactory.optionType(message.getType()),
         JOptionPane.YES_NO_CANCEL_OPTION,
         null,
         options,
         SAVE_OPTION
      );
      JDialog dialog = optionPane.createDialog(parent, message.getTitle());
      installDiscardShortcut(
         dialog.getRootPane(),
         optionPane,
         DISCARD_OPTION,
         Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
      );
      optionPane.selectInitialValue();
      dialog.setVisible(true);
      dialog.dispose();
      return saveDiscardCancelResult(optionPane.getValue());
   }

   static YesNoCancel saveDiscardCancelResult(Object result) {
      if (SAVE_OPTION.equals(result)) {
         return YesNoCancel.YES;
      }
      if (DISCARD_OPTION.equals(result)) {
         return YesNoCancel.NO;
      }
      return YesNoCancel.CANCEL;
   }

   static void installDiscardShortcut(
      JRootPane rootPane,
      JOptionPane optionPane,
      Object discardOption,
      int menuShortcutMask
   ) {
      rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
         .put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, menuShortcutMask), DISCARD_ACTION);
      rootPane.getActionMap().put(DISCARD_ACTION, new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent event) {
            optionPane.setValue(discardOption);
         }
      });
   }

   public static boolean showOkCancelDialog(Component parent, IBasicMessage message) {
      return JOptionPane.showConfirmDialog(parent, message.getText(), message.getTitle(), JOptionPane.OK_CANCEL_OPTION, MessageDialogFactory.optionType(message.getType())) == JOptionPane.OK_OPTION;
   }

   public static boolean showYesNoDialog(Component parent, IBasicMessage message) {
      return JOptionPane.showConfirmDialog(parent, message.getText(), message.getTitle(), JOptionPane.YES_NO_OPTION, MessageDialogFactory.optionType(message.getType())) == JOptionPane.YES_OPTION;
   }
}
