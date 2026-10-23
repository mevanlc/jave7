package de.jave.jave.actions;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

final class ReplaceKeyBinding {
   static KeyStroke create(boolean macOs, int cmdOrCtrlMask) {
      int keyCode = macOs ? KeyEvent.VK_F : KeyEvent.VK_H;
      int modifiers = macOs ? cmdOrCtrlMask | InputEvent.ALT_DOWN_MASK : cmdOrCtrlMask;
      return KeyStroke.getKeyStroke(keyCode, modifiers);
   }

   private ReplaceKeyBinding() {}
}
