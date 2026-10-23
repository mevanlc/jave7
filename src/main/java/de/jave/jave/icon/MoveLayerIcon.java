package de.jave.jave.icon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import javax.swing.Icon;

final class MoveLayerIcon implements Icon {
   private final int size;

   MoveLayerIcon(int size) {
      this.size = size;
   }

   @Override
   public int getIconWidth() {
      return this.size;
   }

   @Override
   public int getIconHeight() {
      return this.size;
   }

   @Override
   public void paintIcon(Component component, Graphics graphics, int x, int y) {
      Graphics2D g = (Graphics2D)graphics.create();
      try {
         g.translate(x, y);
         g.scale(this.size / 16.0, this.size / 16.0);
         g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g.setColor(component == null ? Color.DARK_GRAY : component.getForeground());
         g.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
         for (int direction = 0; direction < 4; direction++) {
            Path2D arrow = new Path2D.Double();
            arrow.moveTo(8, 8);
            arrow.lineTo(8, 1.5);
            arrow.moveTo(5.5, 4);
            arrow.lineTo(8, 1.5);
            arrow.lineTo(10.5, 4);
            g.draw(arrow);
            g.rotate(Math.PI / 2, 8, 8);
         }
      } finally {
         g.dispose();
      }
   }
}
