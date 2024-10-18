package net.disy.commons.core.progress;

public class NonCancelable implements IObservableCancelable {
   private static final NonCancelable instance = new NonCancelable();

   public static NonCancelable getInstance() {
      return instance;
   }

   private NonCancelable() {
   }

   @Override
   public boolean isCanceled() {
      return false;
   }

   @Override
   public void addCanceledListener(ICanceledListener listener) {
   }

   @Override
   public void removeCanceledListener(ICanceledListener listener) {
   }
}
