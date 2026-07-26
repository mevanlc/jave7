package net.dizzy.commons.swing.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JButton;

import org.junit.Test;

public class SmartActionTest {
   @Test
   public void ampersandMarkerConfiguresButtonMnemonic() {
      SmartAction action = createAction("&Replace");

      JButton button = new JButton(action);

      assertEquals("Replace", button.getText());
      assertEquals(KeyEvent.VK_R, button.getMnemonic());
      assertEquals(0, button.getDisplayedMnemonicIndex());
   }

   @Test
   public void escapedAmpersandRemainsLiteral() {
      SmartAction action = createAction("Insert && Close");

      assertEquals("Insert & Close", action.getValue(Action.NAME));
      assertNull(action.getValue(Action.MNEMONIC_KEY));
   }

   @Test
   public void changingNameUpdatesAndClearsMnemonic() {
      SmartAction action = createAction("Open");
      JButton button = new JButton(action);

      action.setName("&Close");
      assertEquals("Close", button.getText());
      assertEquals(KeyEvent.VK_C, button.getMnemonic());

      action.setName("Done");
      assertEquals("Done", button.getText());
      assertEquals(KeyEvent.VK_UNDEFINED, button.getMnemonic());
   }

   private static SmartAction createAction(String name) {
      return new SmartAction(name) {
         @Override
         protected void execute(Component parent) {
         }
      };
   }
}
