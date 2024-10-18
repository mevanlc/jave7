package net.disy.commons.core.progress;

import net.disy.commons.core.model.listener.ListenerList;
import net.disy.commons.core.util.IClosure;

public class DefaultCancelable implements IObservableCancelable {
   private final ListenerList<ICanceledListener> canceledListeners = new ListenerList<>();
   private boolean canceled = false;

   @Override
   public synchronized void addCanceledListener(ICanceledListener listener) {
      this.canceledListeners.add(listener);
   }

   @Override
   public synchronized void removeCanceledListener(ICanceledListener listener) {
      this.canceledListeners.remove(listener);
   }

   @Override
   public boolean isCanceled() {
      return this.canceled;
   }

   public void setCanceled(boolean canceled) {
      if (this.canceled != canceled) {
         this.canceled = canceled;
         if (canceled) {
            this.fireCanceled();
         }
      }
   }

   private void fireCanceled() {
      this.canceledListeners.forAllDo(new IClosure<ICanceledListener>() {
         public void execute(ICanceledListener listener) {
            listener.canceled();
         }
      });
   }
}
