package net.disy.commons.swing.toolbar;

import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.layout.util.LayoutDirection;

public class ToolBarConfiguration implements IToolBarConfiguration {
   private boolean floatable;
   private LayoutDirection orientation;
   private boolean rolloverEffectEnabled = true;

   public ToolBarConfiguration() {
      this(false);
   }

   public ToolBarConfiguration(boolean floatable) {
      this(LayoutDirection.HORIZONTAL, floatable);
   }

   public ToolBarConfiguration(LayoutDirection orientation, boolean floatable) {
      Ensure.ensureArgumentNotNull(orientation);
      this.orientation = orientation;
      this.floatable = floatable;
   }

   @Override
   public boolean isFloatable() {
      return this.floatable;
   }

   public ToolBarConfiguration setFloatable(boolean floatable) {
      this.floatable = floatable;
      return this;
   }

   @Override
   public LayoutDirection getOrientation() {
      return this.orientation;
   }

   public ToolBarConfiguration setOrientation(LayoutDirection orientation) {
      Ensure.ensureArgumentNotNull(orientation);
      this.orientation = orientation;
      return this;
   }

   @Override
   public boolean isRolloverEffectEnabled() {
      return this.rolloverEffectEnabled;
   }

   public ToolBarConfiguration setRolloverEffectEnabled(boolean rolloverEffectEnabled) {
      this.rolloverEffectEnabled = rolloverEffectEnabled;
      return this;
   }
}
