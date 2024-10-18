package net.disy.commons.swing.panel;

import java.awt.LayoutManager;
import java.awt.Rectangle;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public class FixedIncrementVerticalScrollablePanel extends AbstractVerticalScrollablePanel {
   private final int unitIncrement;

   public FixedIncrementVerticalScrollablePanel(LayoutManager layout, int preferredViewPortHeight) {
      this(layout, preferredViewPortHeight, LayoutUtilities.getDpiAdjusted(20));
   }

   private FixedIncrementVerticalScrollablePanel(LayoutManager layout, int preferredViewPortHeight, int unitIncrement) {
      super(layout, preferredViewPortHeight);
      this.unitIncrement = unitIncrement;
   }

   @Override
   protected int getVerticalScrollabelUnitIncrement(Rectangle visibleRect, int direction) {
      return this.unitIncrement;
   }
}
