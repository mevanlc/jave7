package de.jave.image2ascii;

import de.jave.jave.application.resources.JaveImageProvider;
import javax.swing.Icon;
import net.disy.commons.swing.resources.IIconResources;

public class Image2AsciiIcons implements IIconResources {
   public static final Icon ALGORITHM_1PIXEL_ICON = loadIcon("image2ascii/i2a_1.gif");
   public static final Icon ALGORITHM_3D_ICON = loadIcon("image2ascii/i2a_3d.gif");
   public static final Icon ALGORITHM_4PIXEL_ICON = loadIcon("image2ascii/i2a_4.gif");
   public static final Icon ALGORITHM_GRADIENT_ICON = loadIcon("image2ascii/i2a_gradient.gif");
   public static final Icon ALGORITHM_JAVE_ICON = loadIcon("image2ascii/i2a_jave.gif");
   public static final Icon ALGORITHM_KICAD = loadIcon("image2ascii/i2a_kicad.gif");
   public static final Icon ALGORITHM_BRAILLE = loadIcon("image2ascii/i2a_braille.gif");
   public static final Icon IMAGE2ASCII_ICON = loadIcon("image2ascii.gif");

   private static Icon loadIcon(String string) {
      return JaveImageProvider.getInstance().getImageIcon(string);
   }
}
