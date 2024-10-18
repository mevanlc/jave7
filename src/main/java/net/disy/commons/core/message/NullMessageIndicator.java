package net.disy.commons.core.message;

public class NullMessageIndicator implements IMessageIndicator {
   @Override
   public void showMessage(IMessage message) {
   }

   @Override
   public void showMessage(IMessage message, Runnable runAfterShowing) {
      if (runAfterShowing != null) {
         runAfterShowing.run();
      }
   }
}
