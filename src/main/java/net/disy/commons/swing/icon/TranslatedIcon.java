package net.disy.commons.swing.icon;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import net.disy.commons.core.util.Ensure;

public class TranslatedIcon implements Icon {
   private final Icon icon;
   private final int dx;
   private final int dy;

   public TranslatedIcon(Icon icon, int dx, int dy) {
      Ensure.ensureArgumentNotNull(icon);
      this.icon = icon;
      this.dx = dx;
      this.dy = dy;
   }

   @Override
   public int getIconHeight() {
      return this.icon.getIconHeight() + this.dy;
   }

   @Override
   public int getIconWidth() {
      return this.icon.getIconHeight() + this.dx;
   }

   @Override
   public void paintIcon(Component c, Graphics g, int x, int y) {
      this.icon.paintIcon(c, g, x + this.dx, y + this.dy);
   }
}
