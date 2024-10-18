package net.disy.commons.core.model.listener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.IClosure;

public class WeakListenerList<L> {
   private final List<WeakReference<L>> weakListeners;

   public WeakListenerList() {
      this(new ArrayList<>());
   }

   private WeakListenerList(List<WeakReference<L>> weakListeners) {
      Ensure.ensureArgumentNotNull(weakListeners);
      this.weakListeners = weakListeners;
   }

   public synchronized void add(L listener) {
      Ensure.ensureArgumentNotNull(listener);
      WeakReference<L> weakListener = new WeakReference<>(listener);
      this.weakListeners.add(weakListener);
   }

   public synchronized void remove(Object listener) {
      Ensure.ensureArgumentNotNull(listener);
      Iterator<WeakReference<L>> iter = this.weakListeners.iterator();

      while (iter.hasNext()) {
         WeakReference<L> weakListener = iter.next();
         Object currentListener = weakListener.get();
         if (currentListener == null) {
            iter.remove();
         } else if (currentListener == listener) {
            iter.remove();
            return;
         }
      }
   }

   public void forAllDo(IClosure<L> closure) {
      ArrayList<L> activeListeners;
      synchronized (this) {
         activeListeners = new ArrayList<>(this.weakListeners.size());
         Iterator<WeakReference<L>> iter = this.weakListeners.iterator();

         while (iter.hasNext()) {
            L currentListener = iter.next().get();
            if (currentListener == null) {
               iter.remove();
            } else {
               activeListeners.add(currentListener);
            }
         }
      }

      for (L element : activeListeners) {
         closure.execute(element);
      }
   }

   public synchronized WeakListenerList<L> getClone() {
      return new WeakListenerList<>(new ArrayList<>(this.weakListeners));
   }
}
