package de.jave.jave.actions;

import de.jave.gui.CharField;
import java.awt.Component;
import java.awt.Container;
import java.awt.KeyboardFocusManager;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 * When the keyboard focus owner is a text-like widget, the standard
 * cut/copy/paste menu accelerators (Cmd/Ctrl+X/C/V) should act on that
 * widget rather than the canvas editor. The menu accelerators are bound
 * with WHEN_IN_FOCUSED_WINDOW semantics, so they would otherwise fire on
 * the canvas action and the focused widget would never see the key.
 *
 * <p>Resolution order from the focus owner walking up the hierarchy:
 * <ol>
 *   <li>A {@link ClipboardOverride} client property — explicit per-widget
 *       handler (e.g. the Fill tool's pattern preview).</li>
 *   <li>A {@link JTextComponent} on the focus owner — standard text-widget
 *       cut/copy/paste.</li>
 *   <li>A {@link CharField} on the focus owner — single-char clipboard.</li>
 *   <li>The {@link #TOOL_OPTIONS_HOST_PROPERTY} marker on any ancestor —
 *       absorb the keystroke so it doesn't leak to the canvas.</li>
 * </ol>
 */
public final class FocusedTextClipboardDelegate {

   public static final String TOOL_OPTIONS_HOST_PROPERTY = "jave.toolOptionsHost";

   public enum Op {
      CUT, COPY, PASTE
   }

   private FocusedTextClipboardDelegate() {}

   public static boolean tryHandle(Op op) {
      Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
      if (focusOwner == null) {
         return false;
      }
      if (dispatchOverride(focusOwner, op)) {
         return true;
      }
      if (focusOwner instanceof JTextComponent) {
         dispatchTextComponent((JTextComponent)focusOwner, op);
         return true;
      }
      if (focusOwner instanceof CharField) {
         dispatchCharField((CharField)focusOwner, op);
         return true;
      }
      return isInsideToolOptionsHost(focusOwner);
   }

   private static boolean dispatchOverride(Component focusOwner, Op op) {
      for (Component c = focusOwner; c != null; c = c.getParent()) {
         if (c instanceof JComponent) {
            Object value = ((JComponent)c).getClientProperty(ClipboardOverride.CLIENT_PROPERTY);
            if (value instanceof ClipboardOverride) {
               ClipboardOverride override = (ClipboardOverride)value;
               switch (op) {
                  case CUT:
                     override.cut();
                     break;
                  case COPY:
                     override.copy();
                     break;
                  case PASTE:
                     override.paste();
                     break;
               }
               return true;
            }
         }
      }
      return false;
   }

   private static void dispatchTextComponent(JTextComponent textComp, Op op) {
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
   }

   private static void dispatchCharField(CharField field, Op op) {
      switch (op) {
         case COPY:
            field.copy();
            break;
         case CUT:
            field.cut();
            break;
         case PASTE:
            field.paste();
            break;
      }
   }

   private static boolean isInsideToolOptionsHost(Component focusOwner) {
      for (Container c = focusOwner instanceof Container ? (Container)focusOwner : focusOwner.getParent();
           c != null;
           c = c.getParent()) {
         if (c instanceof JComponent
             && Boolean.TRUE.equals(((JComponent)c).getClientProperty(TOOL_OPTIONS_HOST_PROPERTY))) {
            return true;
         }
      }
      return false;
   }
}
