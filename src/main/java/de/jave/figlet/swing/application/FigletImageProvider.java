package de.jave.figlet.swing.application;

import javax.swing.Icon;
import net.dizzy.commons.swing.image.ImageProvider;

public class FigletImageProvider extends ImageProvider {
   private static final FigletImageProvider instance = new FigletImageProvider();
   public static final Icon SPLASH_IMAGE_ICON = getInstance().getImageIcon("figletsplash.png");

   public static final FigletImageProvider getInstance() {
      return instance;
   }

   private FigletImageProvider() {
      super("de/jave/figlet");
   }
}
