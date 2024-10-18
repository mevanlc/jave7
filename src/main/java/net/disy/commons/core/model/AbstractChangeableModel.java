package net.disy.commons.core.model;

import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.model.listener.ListenerList;
import net.disy.commons.core.model.listener.NotifyChangeListenerClosure;

public abstract class AbstractChangeableModel implements Cloneable, IChangeableModel {
   private transient ListenerList<IChangeListener> listeners = new ListenerList<>();

   @Override
   protected Object clone() {
      try {
         AbstractChangeableModel clone = (AbstractChangeableModel)super.clone();
         clone.listeners = new ListenerList<>();
         return clone;
      } catch (CloneNotSupportedException var2) {
         throw new UnreachableCodeReachedException(var2);
      }
   }

   @Override
   public void addChangeListener(IChangeListener listener) {
      this.listeners.add(listener);
   }

   @Override
   public void removeChangeListener(IChangeListener listener) {
      this.listeners.remove(listener);
   }

   protected void fireChangeEvent() {
      this.listeners.forAllDo(NotifyChangeListenerClosure.INSTANCE);
   }

   public int getChangeListenerCount() {
      return this.listeners.getSize();
   }

   protected final Object getMutex() {
      return this.listeners;
   }

   public final void clearListenerList() {
      this.listeners.clear();
   }
}
