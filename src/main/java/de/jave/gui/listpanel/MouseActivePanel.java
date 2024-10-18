package de.jave.gui.listpanel;

import de.jave.gui.GuiTools;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import net.disy.commons.core.util.IBlock;
import net.disy.commons.swing.events.mouse.OverallMouseListeningPanel;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public final class MouseActivePanel extends OverallMouseListeningPanel {
   private Point pressedPoint;
   private boolean inside = false;

   public MouseActivePanel(JComponent content, final IBlock mouseClickBlock) {
      super(content);
      this.setBorder(new EmptyBorder(1, 1, 1, 1 + LayoutUtilities.getDpiAdjusted(8)));
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            MouseActivePanel.this.pressedPoint = e.getPoint();
            MouseActivePanel.this.repaint();
         }

         @Override
         public void mouseExited(MouseEvent e) {
            MouseActivePanel.this.inside = false;
            MouseActivePanel.this.repaint();
         }

         @Override
         public void mouseEntered(MouseEvent e) {
            MouseActivePanel.this.inside = true;
            MouseActivePanel.this.repaint();
         }

         @Override
         public void mouseReleased(MouseEvent e) {
            if (MouseActivePanel.this.pressedPoint != null) {
               MouseActivePanel.this.pressedPoint = null;
               MouseActivePanel.this.repaint();
               mouseClickBlock.execute();
            }
         }
      });
      this.addMouseMotionListener(new MouseAdapter() {
         @Override
         public void mouseDragged(MouseEvent e) {
            Point mousePoint = e.getPoint();
            MouseActivePanel.this.scrollRectToVisible(new Rectangle(mousePoint.x, mousePoint.y, 1, 1));
            if (MouseActivePanel.this.pressedPoint != null && MouseActivePanel.this.isTooFarAwayFromPressedPoint(mousePoint)) {
               MouseActivePanel.this.pressedPoint = null;
               MouseActivePanel.this.repaint();
            }
         }
      });
   }

   private boolean isTooFarAwayFromPressedPoint(Point point) {
      int dx = Math.abs(this.pressedPoint.x - point.x);
      int dy = Math.abs(this.pressedPoint.y - point.y);
      return Math.max(dx, dy) > 4;
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Dimension size = this.getSize();
      int w = size.width - LayoutUtilities.getDpiAdjusted(8);
      if (this.pressedPoint != null) {
         GuiTools.drawSmall3dRectangleDown(g, 0, 0, w, size.height);
      } else if (this.inside) {
         GuiTools.drawSmall3dRectangleUp(g, 0, 0, w, size.height);
      }
   }
}
