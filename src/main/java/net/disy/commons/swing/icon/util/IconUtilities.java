package net.disy.commons.swing.icon.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.Icon;

public class IconUtilities {
   public static BufferedImage createBufferedImage(Icon icon) {
      BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), 2);
      Graphics2D graphics = image.createGraphics();
      icon.paintIcon(null, graphics, 0, 0);
      graphics.dispose();
      return image;
   }
}
