package net.disy.commons.core.model.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.IClosure;

public class ListenerList<L> {
   private final List<L> listeners;

   public ListenerList() {
      this(new ArrayList<>());
   }

   private ListenerList(List<L> listeners) {
      Ensure.ensureArgumentNotNull(listeners);
      this.listeners = listeners;
   }

   public synchronized void add(L listener) {
      Ensure.ensureArgumentNotNull(listener);
      this.listeners.add(listener);
   }

   public synchronized void remove(L listener) {
      this.listeners.remove(listener);
   }

   public void forAllDo(IClosure<L> closure) {
      Collection<L> cloneList;
      synchronized (this) {
         cloneList = new ArrayList<>(this.listeners);
      }

      for (L listener : cloneList) {
         closure.execute(listener);
      }
   }

   public void forAllDoLastListenerFirst(IClosure<L> closure) {
      List<L> cloneList;
      synchronized (this) {
         cloneList = new ArrayList<>(this.listeners);
      }

      Collections.reverse(cloneList);

      for (L listener : cloneList) {
         closure.execute(listener);
      }
   }

   public synchronized boolean contains(L listener) {
      return this.listeners.contains(listener);
   }

   public synchronized void clear() {
      this.listeners.clear();
   }

   public synchronized boolean isEmpty() {
      return this.listeners.isEmpty();
   }

   public synchronized int getSize() {
      return this.listeners.size();
   }
}
