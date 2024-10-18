package de.jave.lib.tree.checkbox;

public class CheckBoxNodeRenderState {
   private final boolean enabled;
   private final VisibleCheckBoxState state;

   public CheckBoxNodeRenderState(VisibleCheckBoxState state, boolean enabled) {
      this.enabled = enabled;
      this.state = state;
   }

   @Override
   public String toString() {
      return "CheckBoxNodeRenderState{" + this.state + "," + this.enabled + "}";
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public boolean isSelected() {
      return this.state == VisibleCheckBoxState.SELECTED;
   }

   public boolean isPartialSelected() {
      return this.state == VisibleCheckBoxState.PARTIAL_SELECTED;
   }
}
