package de.jave.gui.splash;

import de.jave.lib.gui.GuiUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JWindow;
import net.disy.commons.core.model.ObjectModel;

public class FallbackSplashWindow implements ISplashWindow {
   private final ObjectModel<String> textModel = new ObjectModel<>("");
   private final JWindow window;

   public FallbackSplashWindow(ISplashScreenSetup setup) {
      Icon splashImage = setup.getSplashImageIcon();
      this.window = new JWindow();
      this.window.getContentPane().setLayout(new BorderLayout());
      JComponent component = SplashComponentUtilities.createTextOverlayedComponent(splashImage, setup.getProgressLabelArea(), this.textModel);
      this.window.getContentPane().add(component, "Center");
      this.window.pack();
      GuiUtilities.centerOnScreen(this.window);
      this.window.setVisible(true);
   }

   @Override
   public void setProgressText(String text) {
      this.textModel.setValue(text);
   }

   @Override
   public void dispose() {
      this.window.dispose();
   }

   @Override
   public Component getOptionalComponentForParent() {
      return this.window;
   }
}
