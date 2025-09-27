package net.disy.commons.swing.icon;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;
import net.disy.commons.swing.graphics.ColorFilterGraphics;

public class DisabledIconDecorator implements Icon {
   private final Icon icon;

   public DisabledIconDecorator(Icon icon) {
      this.icon = icon;
   }

   @Override
   public int getIconHeight() {
      return this.icon.getIconHeight();
   }

   @Override
   public int getIconWidth() {
      return this.icon.getIconWidth();
   }

   @Override
   public void paintIcon(Component c, Graphics g, int x, int y) {
      ColorFilterGraphics disabledGraphics = new ColorFilterGraphics((Graphics2D)g, c);
      this.icon.paintIcon(c, disabledGraphics, x, y);
   }
}
