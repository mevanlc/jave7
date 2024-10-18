package de.jave.gui.splash;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import net.disy.commons.core.util.Ensure;

public class AwtSplashScreenWindow implements ISplashWindow {
   private final SplashScreen splash;
   private final ISplashScreenSetup setup;

   public AwtSplashScreenWindow(SplashScreen splash, ISplashScreenSetup setup) {
      Ensure.ensureArgumentNotNull(splash);
      Ensure.ensureArgumentNotNull(setup);
      this.splash = splash;
      this.setup = setup;
   }

   @Override
   public void dispose() {
      if (this.splash.isVisible()) {
         this.splash.close();
      }
   }

   @Override
   public void setProgressText(String text) {
      if (this.splash.isVisible()) {
         Graphics2D g = this.splash.createGraphics();
         SplashComponentUtilities.renderLabel(g, text, this.setup.getProgressLabelArea());
         g.dispose();
         this.splash.update();
      }
   }

   @Override
   public Component getOptionalComponentForParent() {
      return null;
   }
}
