package de.jave.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.SystemColor;
import javax.swing.JComponent;

public class SmallFrameLeftBorder extends JComponent {
   @Override
   public Dimension getPreferredSize() {
      return new Dimension(3, 10);
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
      g.drawLine(0, 0, 0, d.height - 1);
      g.setColor(SystemColor.controlLtHighlight);
      g.drawLine(1, 0, 1, d.height - 1);
      g.setColor(SystemColor.control);
      g.drawLine(2, 0, 2, d.height - 1);
   }
}
