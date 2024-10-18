package net.disy.commons.swing.toolbar;

import net.disy.commons.swing.layout.util.LayoutDirection;

public interface IToolBarConfiguration {
   boolean isFloatable();

   LayoutDirection getOrientation();

   boolean isRolloverEffectEnabled();
}
