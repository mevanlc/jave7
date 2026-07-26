package de.jave.jave.actions;

import static org.junit.Assert.assertEquals;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import org.junit.Test;

public class JaveKeyBindingsTest {
   @Test
   public void replaceUsesMenuOptionFOnMacOs() {
      KeyStroke shortcut = ReplaceKeyBinding.create(true, InputEvent.META_DOWN_MASK);

      assertEquals(
         KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.META_DOWN_MASK | InputEvent.ALT_DOWN_MASK),
         shortcut
      );
   }

   @Test
   public void replaceKeepsMenuHOnOtherPlatforms() {
      KeyStroke shortcut = ReplaceKeyBinding.create(false, InputEvent.CTRL_DOWN_MASK);

      assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK), shortcut);
   }
}
