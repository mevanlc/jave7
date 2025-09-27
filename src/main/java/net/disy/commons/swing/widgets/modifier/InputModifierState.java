package net.disy.commons.swing.events.modifier;

import java.awt.event.InputEvent;
import net.disy.commons.core.util.Ensure;

public class InputModifierState {
   private static final String EMPTY = "";
   private final int modifiers;

   public InputModifierState() {
      this.modifiers = 0;
   }

   public InputModifierState(InputEvent event) {
      Ensure.ensureArgumentNotNull(event);
      this.modifiers = event.getModifiers();
   }

   public boolean isShiftDown() {
      return (this.modifiers & 1) != 0;
   }

   public boolean isControlDown() {
      return (this.modifiers & 2) != 0;
   }

   public boolean isMetaDown() {
      return (this.modifiers & 4) != 0;
   }

   public boolean isAltDown() {
      return (this.modifiers & 8) != 0;
   }

   @Override
   public String toString() {
      return "InputModifierState{"
         + (this.isShiftDown() ? "S" : "")
         + (this.isAltDown() ? "A" : "")
         + (this.isControlDown() ? "C" : "")
         + (this.isMetaDown() ? "M" : "")
         + "}";
   }

   public boolean isEqualState(InputEvent e) {
      return e.getModifiers() == this.modifiers;
   }
}
