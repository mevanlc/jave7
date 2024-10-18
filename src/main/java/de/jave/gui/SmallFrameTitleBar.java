package de.jave.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;

public class SmallFrameTitleBar extends JComponent implements MouseListener, MouseMotionListener {
   private Point lastAnchorPoint;
   private static final Font FONT = new Font("Dialog", 1, 10);
   private String title;

   public SmallFrameTitleBar(String title) {
      this.addMouseMotionListener(this);
      this.addMouseListener(this);
      this.setFont(FONT);
      this.title = title;
   }

   public void setTitle(String title) {
      this.title = title;
      this.repaint();
   }

   public String getTitle() {
      return this.title;
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(50, 14);
   }

   @Override
   public Dimension getMinimumSize() {
      return this.getPreferredSize();
   }

   @Override
   public Dimension getMaximumSize() {
      return this.getPreferredSize();
   }

   @Override
   protected void paintComponent(Graphics g) {
      Dimension d = this.getSize();
      g.setColor(SystemColor.controlHighlight);
      g.drawLine(0, 0, d.width - 2, 0);
      g.drawLine(0, 1, 0, d.height - 1);
      g.setColor(SystemColor.controlLtHighlight);
      g.drawLine(1, 1, d.width - 3, 1);
      g.drawLine(1, 2, 1, d.height - 1);
      g.setColor(SystemColor.controlShadow);
      g.drawLine(d.width - 2, 1, d.width - 2, d.height - 1);
      g.setColor(SystemColor.controlDkShadow);
      g.drawLine(d.width - 1, 0, d.width - 1, d.height - 1);
      g.setColor(SystemColor.activeCaption);
      g.fillRect(2, 2, d.width - 4, d.height - 3);
      g.setColor(SystemColor.activeCaption);
      g.drawLine(2, d.height - 1, d.width - 3, d.height - 1);
      FontMetrics fm = g.getFontMetrics(this.getFont());
      int maxWidth = d.width - 3;
      String label = this.title;
      if (fm.stringWidth(label) + 4 > maxWidth) {
         while (label.length() > 3 && fm.stringWidth(label + "...") + 4 > maxWidth) {
            label = label.substring(0, label.length() - 1);
         }

         label = label + "...";
      }

      g.setColor(SystemColor.activeCaptionText);
      g.drawString(label, 4, 11);
   }

   private void moveWindow(int dx, int dy) {
      if (dx != 0 || dy != 0) {
         Container con = this.getParent();

         while (con != null && !(con instanceof Window)) {
            con = con.getParent();
         }

         if (con != null) {
            Point p = con.getLocationOnScreen();
            con.setLocation(p.x + dx, p.y + dy);
         }
      }
   }

   private Window getParentWindow() {
      Container con = this.getParent();

      while (con != null && !(con instanceof Window)) {
         con = con.getParent();
      }

      return (Window)con;
   }

   @Override
   public void mouseMoved(MouseEvent evt) {
   }

   @Override
   public void mouseClicked(MouseEvent evt) {
   }

   @Override
   public void mouseEntered(MouseEvent evt) {
   }

   @Override
   public void mouseExited(MouseEvent evt) {
   }

   @Override
   public void mouseReleased(MouseEvent evt) {
      this.lastAnchorPoint = null;
   }

   @Override
   public void mousePressed(MouseEvent evt) {
      Point p1 = evt.getPoint();
      Window w = this.getParentWindow();
      if (w == null) {
         this.lastAnchorPoint = null;
      } else {
         Point p2 = w.getLocationOnScreen();
         this.lastAnchorPoint = new Point(p1.x + p2.x, p1.y + p2.y);
      }
   }

   @Override
   public void mouseDragged(MouseEvent evt) {
      if (this.lastAnchorPoint != null) {
         Point p1 = evt.getPoint();
         Window w = this.getParentWindow();
         if (w == null) {
            this.lastAnchorPoint = null;
         } else {
            Point p2 = w.getLocationOnScreen();
            Point p = new Point(p1.x + p2.x, p1.y + p2.y);
            this.moveWindow(p.x - this.lastAnchorPoint.x, p.y - this.lastAnchorPoint.y);
            this.lastAnchorPoint = p;
         }
      }
   }
}
