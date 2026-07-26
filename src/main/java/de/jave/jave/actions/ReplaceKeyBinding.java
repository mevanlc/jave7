package de.jave.jave.actions;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

final class ReplaceKeyBinding {
   static KeyStroke create(boolean macOs, int menuShortcutMask) {
      int keyCode = macOs ? KeyEvent.VK_F : KeyEvent.VK_H;
      int modifiers = macOs ? menuShortcutMask | InputEvent.ALT_DOWN_MASK : menuShortcutMask;
      return KeyStroke.getKeyStroke(keyCode, modifiers);
   }

   private ReplaceKeyBinding() {}
}
