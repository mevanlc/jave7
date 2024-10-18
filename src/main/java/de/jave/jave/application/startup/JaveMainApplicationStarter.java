package de.jave.jave.application.startup;

import de.jave.gui.splash.SplashScreenManager;
import de.jave.jave.JaveMessages;
import de.jave.jave.version.JaveVersion;
import de.jave.maxosx.MacOsXInitializer;
import java.text.MessageFormat;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.disy.commons.core.exception.CentralExceptionHandling;
import net.disy.commons.core.exception.IExceptionHandler;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;
import net.disy.commons.swing.dialog.userdialog.UserDialog;

public class JaveMainApplicationStarter {
   public static void startJaveApplication(final String[] arguments) {
      String applicationName = MessageFormat.format("JavE {0}", JaveVersion.getFullVersionNumber());
      MacOsXInitializer.setApplicationNameProperty(applicationName);
      CentralExceptionHandling.setHandler(
         new IExceptionHandler() {
            @Override
            public void handle(Throwable exception) {
               exception.printStackTrace();
               UserDialog dialog = MessageDialogFactory.createMessageDialog(
                  null, new Message(JaveMessages.DefaultExceptionHandler_Title, JaveMessages.DefaultExceptionHandler_Text, MessageType.ERROR, exception)
               );
               dialog.getDialog().setModal(false);
               dialog.show();
            }
         }
      );
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception var2) {
            }

            JPopupMenu.setDefaultLightWeightPopupEnabled(false);
            SplashScreenManager splash = new SplashScreenManager(new JavESplashScreenSetup());
            splash.startup(new JaveStartupRunnable(arguments));
         }
      });
   }
}
