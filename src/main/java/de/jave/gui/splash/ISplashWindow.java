package de.jave.gui.splash;

import java.awt.Component;

public interface ISplashWindow {
   void setProgressText(String var1);

   void dispose();

   Component getOptionalComponentForParent();
}
