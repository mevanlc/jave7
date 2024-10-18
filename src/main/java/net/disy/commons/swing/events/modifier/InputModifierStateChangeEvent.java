package net.disy.commons.swing.events.modifier;

import java.util.EventObject;
import net.disy.commons.core.util.Ensure;

public class InputModifierStateChangeEvent extends EventObject {
   private final InputModifierState state;

   public InputModifierStateChangeEvent(InputModifierState state) {
      super(state);
      Ensure.ensureArgumentNotNull(state);
      this.state = state;
   }

   public InputModifierState getState() {
      return this.state;
   }
}
