package de.jave.jave.application.startup;

import de.jave.gui.splash.ISplashScreenSetup;
import de.jave.jave.icon.JaveIcons;
import java.awt.Rectangle;
import javax.swing.Icon;

public class JavESplashScreenSetup implements ISplashScreenSetup {
   private static final Rectangle PROGRESS_LABEL_AREA = new Rectangle(2, 185, 342, 15);

   @Override
   public Rectangle getProgressLabelArea() {
      return PROGRESS_LABEL_AREA;
   }

   @Override
   public Icon getSplashImageIcon() {
      return JaveIcons.SPLASH_IMAGE_ICON;
   }
}
