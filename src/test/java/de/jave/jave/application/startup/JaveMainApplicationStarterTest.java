package de.jave.jave.application.startup;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import net.disy.commons.swing.dialog.core.DialogResult;
import net.disy.commons.swing.dialog.userdialog.IDialogCloseHandler;
import org.junit.Assert;
import org.junit.Test;

public class JaveMainApplicationStarterTest {
   @Test
   public void defaultExceptionHandlerShowsOnlyOneDialogUntilClosed() throws Exception {
      RecordingExceptionHandler handler = new RecordingExceptionHandler();

      runOnEventDispatchThread(new Runnable() {
         @Override
         public void run() {
            handler.handle(new RuntimeException("first"));
            handler.handle(new RuntimeException("second"));
         }
      });

      Assert.assertEquals(1, handler.dialogCount);

      handler.closeHandler.handleDialogClose(new DialogResult(true));

      runOnEventDispatchThread(new Runnable() {
         @Override
         public void run() {
            handler.handle(new RuntimeException("third"));
         }
      });

      Assert.assertEquals(2, handler.dialogCount);
   }

   private static void runOnEventDispatchThread(Runnable runnable) throws InvocationTargetException, InterruptedException {
      if (SwingUtilities.isEventDispatchThread()) {
         runnable.run();
      } else {
         SwingUtilities.invokeAndWait(runnable);
      }
   }

   private static class RecordingExceptionHandler extends JaveMainApplicationStarter.DefaultExceptionHandler {
      private int dialogCount;
      private IDialogCloseHandler closeHandler;

      @Override
      void showDialog(Throwable exception) {
         this.dialogCount++;
         this.closeHandler = new IDialogCloseHandler() {
            @Override
            public void handleDialogClose(net.disy.commons.swing.dialog.core.IDialogResult result) {
               clearShowingDialog();
            }
         };
      }
   }
}
