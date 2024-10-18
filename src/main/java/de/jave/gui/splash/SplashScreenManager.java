package de.jave.gui.splash;

import java.awt.Component;
import java.awt.SplashScreen;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;

public class SplashScreenManager {
   private static final long MIN_VISIBLE_TIME = 1500L;
   private final ISplashWindow window;

   public SplashScreenManager(ISplashScreenSetup setup) {
      Ensure.ensureArgumentNotNull(setup);
      SplashScreen awtSplash = SplashScreen.getSplashScreen();
      if (awtSplash == null) {
         this.window = new FallbackSplashWindow(setup);
      } else {
         this.window = new AwtSplashScreenWindow(awtSplash, setup);
      }
   }

   public void startup(IStartupRunnable runnable) {
      long startTime = System.currentTimeMillis();
      Thread.yield();

      try {
         runnable.startUp(new IStartupMonitor() {
            @Override
            public void beginTask(String taskName, int totalWork) {
               SplashScreenManager.this.window.setProgressText(taskName);
               Thread.yield();
            }

            @Override
            public void subTask(String name) {
               SplashScreenManager.this.window.setProgressText(name);
               Thread.yield();
            }

            @Override
            public void dispose() {
               SplashScreenManager.this.window.dispose();
            }

            @Override
            public Component getParentComponent() {
               return SplashScreenManager.this.window.getOptionalComponentForParent();
            }
         });
         long time = System.currentTimeMillis() - startTime;
         if (time < 1500L) {
            try {
               Thread.sleep(1500L - time);
            } catch (InterruptedException var7) {
            }
         }

         this.window.dispose();
      } catch (StartupException var8) {
         var8.printStackTrace();
         String message = var8.getLocalizedMessage();
         if (message == null || message.length() == 0) {
            message = var8.toString();
         }

         this.window.dispose();
         MessageDialogFactory.showMessageDialog(null, new Message(message, var8));
         System.exit(1);
      } catch (Throwable var9) {
         var9.printStackTrace();
         String message = var9.getLocalizedMessage();
         if (message == null || message.length() == 0) {
            message = var9.toString();
         }

         this.window.dispose();
         MessageDialogFactory.showMessageDialog(null, new Message(message, var9));
         System.exit(1);
      }
   }
}
