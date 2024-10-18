package net.disy.commons.core.progress;

import java.io.InterruptedIOException;

public class ProgressUtilities {
   public static void checkInterrupted(ICancelable cancelable) throws InterruptedException {
      if (cancelable.isCanceled()) {
         throw new InterruptedException();
      }
   }

   public static void checkInterruptedIO(ICancelable cancelable) throws InterruptedIOException {
      if (cancelable.isCanceled()) {
         throw new InterruptedIOException();
      }
   }
}
