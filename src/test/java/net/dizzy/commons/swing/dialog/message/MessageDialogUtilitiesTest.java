package net.dizzy.commons.swing.dialog.message;

import static org.junit.Assert.assertEquals;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import org.junit.Test;

public class MessageDialogUtilitiesTest {
   @Test
   public void saveDiscardCancelResultsKeepTheirButtonSemantics() {
      assertEquals(YesNoCancel.YES, MessageDialogUtilities.saveDiscardCancelResult("Save"));
      assertEquals(YesNoCancel.NO, MessageDialogUtilities.saveDiscardCancelResult("Don't Save"));
      assertEquals(YesNoCancel.CANCEL, MessageDialogUtilities.saveDiscardCancelResult("Cancel"));
      assertEquals(YesNoCancel.CANCEL, MessageDialogUtilities.saveDiscardCancelResult(null));
   }

   @Test
   public void primaryBackspaceSelectsTheConfiguredDiscardAnswer() {
      assertDiscardShortcutSelects(YesNoCancel.NO, InputEvent.CTRL_DOWN_MASK);
      assertDiscardShortcutSelects(YesNoCancel.YES, InputEvent.CTRL_DOWN_MASK);
      assertDiscardShortcutSelects(YesNoCancel.NO, InputEvent.META_DOWN_MASK);
      assertDiscardShortcutSelects(YesNoCancel.YES, InputEvent.META_DOWN_MASK);
   }

   private static void assertDiscardShortcutSelects(YesNoCancel discardAnswer, int shortcutMask) {
      JRootPane rootPane = new JRootPane();
      JOptionPane optionPane = new JOptionPane();
      MessageDialogUtilities.installDiscardShortcut(rootPane, optionPane, discardAnswer, shortcutMask);

      Object actionKey = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
         .get(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, shortcutMask));
      Action action = rootPane.getActionMap().get(actionKey);
      action.actionPerformed(new ActionEvent(rootPane, ActionEvent.ACTION_PERFORMED, "discard"));

      assertEquals(discardAnswer, optionPane.getValue());
   }
}
