package de.jave.jave.actions;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

final class MoveLayerKeyBinding {
   static KeyStroke create(boolean macOs) {
      int modifiers = macOs ? InputEvent.CTRL_DOWN_MASK : InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK;
      return KeyStroke.getKeyStroke(KeyEvent.VK_V, modifiers);
   }

   private MoveLayerKeyBinding() {}
}
