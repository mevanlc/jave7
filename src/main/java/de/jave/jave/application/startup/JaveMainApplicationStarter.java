package de.jave.jave.application.startup;

import de.jave.jave.JaveMessages;
import de.jave.jave.version.JaveVersion;
import de.jave.maxosx.MacOsXInitializer;
import java.text.MessageFormat;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.dizzy.commons.core.exception.CentralExceptionHandling;
import net.dizzy.commons.core.exception.IExceptionHandler;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.swing.dialog.core.IDialogResult;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;
import net.dizzy.commons.swing.dialog.userdialog.IDialogCloseHandler;
import net.dizzy.commons.swing.dialog.userdialog.UserDialog;

public class JaveMainApplicationStarter {
   public static void startJaveApplication(final String[] arguments) {
      String applicationName = MessageFormat.format("JavE {0}", JaveVersion.getFullVersionNumber());
      MacOsXInitializer.setApplicationNameProperty(applicationName);
      CentralExceptionHandling.setHandler(new DefaultExceptionHandler());
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception var2) {
            }

            JPopupMenu.setDefaultLightWeightPopupEnabled(false);
            try {
               new JaveStartup().start();
            } catch (Throwable exception) {
               handleStartupFailure(exception);
            }
         }
      });
   }

   private static void handleStartupFailure(Throwable exception) {
      exception.printStackTrace();
      String message = exception.getLocalizedMessage();
      if (message == null || message.length() == 0) {
         message = exception.toString();
      }
      MessageDialogFactory.showMessageDialog(null, new Message(message, exception));
      System.exit(1);
   }

   static class DefaultExceptionHandler implements IExceptionHandler {
      private boolean showingDialog;

      @Override
      public void handle(final Throwable exception) {
         exception.printStackTrace();
         if (!markShowingDialog()) {
            return;
         }
         Runnable showDialog = new Runnable() {
            @Override
            public void run() {
               showDialog(exception);
            }
         };
         if (SwingUtilities.isEventDispatchThread()) {
            showDialog.run();
         } else {
            SwingUtilities.invokeLater(showDialog);
         }
      }

      private synchronized boolean markShowingDialog() {
         if (this.showingDialog) {
            return false;
         }
         this.showingDialog = true;
         return true;
      }

      protected synchronized void clearShowingDialog() {
         this.showingDialog = false;
      }

      void showDialog(Throwable exception) {
         try {
            UserDialog dialog = MessageDialogFactory.createMessageDialog(
               null, new Message(JaveMessages.DefaultExceptionHandler_Title, JaveMessages.DefaultExceptionHandler_Text, MessageType.ERROR, exception)
            );
            dialog.showNonModal(new IDialogCloseHandler() {
               @Override
               public void handleDialogClose(IDialogResult result) {
                  clearShowingDialog();
               }
            });
         } catch (Throwable dialogException) {
            clearShowingDialog();
            dialogException.printStackTrace();
         }
      }
   }
}
