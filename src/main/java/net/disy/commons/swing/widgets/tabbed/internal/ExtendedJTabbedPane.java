package net.disy.commons.swing.dialog.tabbed.internal;

import javax.swing.JTabbedPane;
import net.disy.commons.swing.dialog.tabbed.TabPlacement;

public class ExtendedJTabbedPane extends JTabbedPane {
   private boolean popupMenuVisible;
   private final TabPlacement placement;

   public ExtendedJTabbedPane(TabPlacement placement, int tabLayoutPolicy) {
      super(placement.getSwingValue(), tabLayoutPolicy);
      this.placement = placement;
   }

   public void setHasPopupMenuVisible(boolean popupMenuVisible) {
      this.popupMenuVisible = popupMenuVisible;
   }

   public boolean isPopupMenuVisible() {
      return this.popupMenuVisible;
   }

   public TabPlacement getTabTitlePlacement() {
      return this.placement;
   }
}
