package net.dizzy.commons.core.progress;

public final class ProgressUtilities {
   private ProgressUtilities() {
   }

   public static void checkInterrupted(ICancelable cancelable) throws InterruptedException {
      if ((cancelable != null && cancelable.isCanceled()) || Thread.interrupted()) {
         throw new InterruptedException();
      }
   }
}
