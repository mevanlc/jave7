package net.disy.commons.swing.icon;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.Icon;

public class EmptyIcon implements Icon {
   public static final EmptyIcon DEFAULT_ICON = new EmptyIcon(new Dimension(16, 16));
   private final Dimension size;

   public EmptyIcon() {
      this(new Dimension(0, 0));
   }

   public EmptyIcon(Dimension size) {
      this.size = size;
   }

   @Override
   public int getIconHeight() {
      return this.size.height;
   }

   @Override
   public int getIconWidth() {
      return this.size.width;
   }

   @Override
   public void paintIcon(Component c, Graphics g, int x, int y) {
   }
}
