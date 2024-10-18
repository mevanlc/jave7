package net.disy.commons.swing.component;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import net.disy.commons.swing.color.SwingColors;

public class VerticalLine extends JComponent {
   private static final Dimension SIZE = new Dimension(7, 18);

   @Override
   public Dimension getMinimumSize() {
      return this.getPreferredSize();
   }

   @Override
   public Dimension getMaximumSize() {
      return this.getPreferredSize();
   }

   @Override
   public Dimension getPreferredSize() {
      return SIZE;
   }

   @Override
   public void paintComponent(Graphics g) {
      int height = this.getSize().height;
      int gap = (height - 18) / 2;
      int x = 3;
      g.setColor(SwingColors.getControlLtHighlightColor());
      g.drawLine(4, gap, 4, height - gap);
      g.setColor(SwingColors.getControlShadowColor());
      g.drawLine(3, gap, 3, height - gap);
      g.drawLine(4, gap, 4, gap);
   }
}
