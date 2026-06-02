package de.jave.jave.application.startup;

import de.jave.gui.splash.SplashComponentUtilities;
import de.jave.jave.version.JaveVersion;
import javax.swing.JComponent;
import net.dizzy.commons.core.model.ObjectModel;

public class JaveSplashComponentFactory {
   public static JComponent createAboutLogoComponent() {
      String text = "JavE " + JaveVersion.getFullVersionNumber() + " " + JaveVersion.getBuildDate();
      ObjectModel<String> textModel = new ObjectModel<>(text);
      JavESplashScreenSetup setup = new JavESplashScreenSetup();
      return SplashComponentUtilities.createTextOverlayedComponent(setup.getSplashImageIcon(), setup.getProgressLabelArea(), textModel);
   }
}
