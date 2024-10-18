package net.disy.commons.core.model;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.model.listener.ListenerList;
import net.disy.commons.core.model.listener.NotifyChangeListenerClosure;
import net.disy.commons.core.util.ObjectUtilities;

public abstract class SmartChangeableModel implements Cloneable, IChangeableModel {
   private transient ListenerList<IChangeListener> listeners = new ListenerList<>();
   private transient ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

   @Override
   protected Object clone() {
      try {
         SmartChangeableModel clone = (SmartChangeableModel)super.clone();
         clone.listeners = new ListenerList<>();
         clone.readWriteLock = new ReentrantReadWriteLock();
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

   public final void clearListenerList() {
      this.listeners.clear();
   }

   protected <T> T getValue(SmartChangeableModel.IProperty<T> property) {
      Lock readLock = this.readWriteLock.readLock();
      readLock.lock();

      Object var3;
      try {
         var3 = property.getValue();
      } finally {
         readLock.unlock();
      }

      return (T)var3;
   }

   protected final <T> void setValue(SmartChangeableModel.IProperty<T> property, T value) {
      Lock writeLock = this.readWriteLock.writeLock();
      writeLock.lock();

      label33: {
         try {
            if (!ObjectUtilities.equals(property.getValue(), value)) {
               property.setValue(value);
               break label33;
            }
         } finally {
            writeLock.unlock();
         }

         return;
      }

      this.fireChangeEvent();
   }

   protected interface IProperty<T> {
      T getValue();

      void setValue(T var1);
   }
}
