package net.disy.commons.swing.panel;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.Scrollable;

public abstract class AbstractVerticalScrollablePanel extends JPanel implements Scrollable {
   private final int preferredViewPortHeight;

   public AbstractVerticalScrollablePanel(LayoutManager layout, int preferredViewPortHeight) {
      super(layout);
      this.preferredViewPortHeight = preferredViewPortHeight;
   }

   @Override
   public final boolean getScrollableTracksViewportHeight() {
      return false;
   }

   @Override
   public final boolean getScrollableTracksViewportWidth() {
      return true;
   }

   @Override
   public final Dimension getPreferredScrollableViewportSize() {
      return new Dimension(1, Math.min(this.preferredViewPortHeight, this.getPreferredSize().height));
   }

   @Override
   public final int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
      return orientation == 0 ? visibleRect.width : visibleRect.height;
   }

   @Override
   public final int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
      return orientation == 0 ? visibleRect.width : this.getVerticalScrollabelUnitIncrement(visibleRect, direction);
   }

   protected abstract int getVerticalScrollabelUnitIncrement(Rectangle var1, int var2);
}
