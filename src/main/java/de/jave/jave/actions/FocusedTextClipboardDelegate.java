package de.jave.jave.actions;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import javax.swing.text.JTextComponent;

/**
 * When the keyboard focus owner is a {@link JTextComponent}, the standard
 * cut/copy/paste menu accelerators (Cmd/Ctrl+X/C/V) should act on that
 * widget rather than the canvas editor. The menu accelerators are bound
 * with WHEN_IN_FOCUSED_WINDOW semantics, so they would otherwise fire on
 * the canvas action and the focused text widget would never see the key.
 */
public final class FocusedTextClipboardDelegate {

   public enum Op {
      CUT, COPY, PASTE
   }

   private FocusedTextClipboardDelegate() {}

   public static boolean tryHandle(Op op) {
      Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
      if (!(focusOwner instanceof JTextComponent)) {
         return false;
      }
      JTextComponent textComp = (JTextComponent) focusOwner;
      switch (op) {
         case COPY:
            textComp.copy();
            break;
         case CUT:
            if (textComp.isEditable() && textComp.isEnabled()) {
               textComp.cut();
            }
            break;
         case PASTE:
            if (textComp.isEditable() && textComp.isEnabled()) {
               textComp.paste();
            }
            break;
      }
      return true;
   }
}
