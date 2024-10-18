package de.jave.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.SystemColor;
import javax.swing.JComponent;

class SmallFrameBottomBorder extends JComponent {
   public SmallFrameBottomBorder() {
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(10, 3);
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
      g.setColor(SystemColor.control);
      g.drawLine(2, 0, d.width - 3, 0);
      g.setColor(SystemColor.controlDkShadow);
      g.drawLine(0, 2, d.width - 1, 2);
      g.drawLine(d.width - 1, 0, d.width - 1, 1);
      g.setColor(SystemColor.controlShadow);
      g.drawLine(1, 1, d.width - 2, 1);
      g.drawLine(d.width - 2, 0, d.width - 2, 0);
      g.setColor(SystemColor.controlHighlight);
      g.drawLine(0, 0, 0, 1);
      g.setColor(SystemColor.controlLtHighlight);
      g.drawLine(1, 0, 1, 0);
   }
}
