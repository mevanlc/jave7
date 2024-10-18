package net.disy.commons.swing.popup;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.util.GuiUtilities;

public class PopupMenuMouseListener extends MouseAdapter {
   private boolean upperLeft = false;
   private final IPopupMenuFactory popupMenuFactory;

   public PopupMenuMouseListener(IPopupMenuFactory popupMenuFactory) {
      Ensure.ensureArgumentNotNull(popupMenuFactory);
      this.popupMenuFactory = popupMenuFactory;
   }

   public void setUpperLeftCorner(boolean upperLeft) {
      this.upperLeft = upperLeft;
   }

   public boolean isUpperLeftCorner() {
      return this.upperLeft;
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      if (e.isPopupTrigger()) {
         if (!this.upperLeft) {
            this.show(e.getComponent(), e.getX(), e.getY());
         } else {
            Component c = e.getComponent();
            Dimension d = c.getSize();
            this.show(c, 0, d.height);
         }
      }
   }

   @Override
   public void mousePressed(MouseEvent e) {
      if (e.isPopupTrigger()) {
         if (!this.upperLeft) {
            this.show(e.getComponent(), e.getX(), e.getY());
         } else {
            Component c = e.getComponent();
            Dimension d = c.getSize();
            this.show(c, 0, d.height);
         }
      }
   }

   private void show(Component component, int x, int y) {
      JPopupMenu popup = this.popupMenuFactory.createPopupMenu();
      if (popup != null && component.isShowing()) {
         GuiUtilities.showFitToScreen(popup, (JComponent)component, x, y);
      }
   }
}
