package net.dizzy.commons.swing.icon.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

public final class IconUtilities {
   private IconUtilities() {
   }

   public static BufferedImage createBufferedImage(Icon icon) {
      BufferedImage image = new BufferedImage(Math.max(1, icon.getIconWidth()), Math.max(1, icon.getIconHeight()), BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics = image.createGraphics();
      try {
         icon.paintIcon(null, graphics, 0, 0);
      } finally {
         graphics.dispose();
      }
      return image;
   }
}
