package net.dizzy.commons.core.model.listener;

import java.util.ArrayList;
import java.util.List;

import net.dizzy.commons.core.util.IClosure;

public class ListenerList<T> {
   private final List<T> listeners = new ArrayList<>();

   public void add(T listener) {
      if (listener != null && !listeners.contains(listener)) {
         listeners.add(listener);
      }
   }

   public void remove(T listener) {
      listeners.remove(listener);
   }

   public void forAllDo(IClosure<T> closure) {
      for (T listener : new ArrayList<>(listeners)) {
         closure.execute(listener);
      }
   }
}
