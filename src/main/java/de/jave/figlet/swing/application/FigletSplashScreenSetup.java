package de.jave.figlet.swing.application;

import de.jave.gui.splash.ISplashScreenSetup;
import java.awt.Rectangle;
import javax.swing.Icon;

public final class FigletSplashScreenSetup implements ISplashScreenSetup {
   @Override
   public Icon getSplashImageIcon() {
      return FigletImageProvider.SPLASH_IMAGE_ICON;
   }

   @Override
   public Rectangle getProgressLabelArea() {
      return new Rectangle(2, 102, 247, 15);
   }
}
