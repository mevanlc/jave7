package net.dizzy.commons.core.model;

import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.model.listener.ListenerList;
import net.dizzy.commons.core.util.IClosure;

public abstract class AbstractChangeableModel implements IChangeableModel {
   private final ListenerList<IChangeListener> changeListeners = new ListenerList<>();

   @Override
   public void addChangeListener(IChangeListener listener) {
      changeListeners.add(listener);
   }

   @Override
   public void removeChangeListener(IChangeListener listener) {
      changeListeners.remove(listener);
   }

   protected void fireChangeEvent() {
      changeListeners.forAllDo(new IClosure<IChangeListener>() {
         @Override
         public void execute(IChangeListener listener) {
            listener.stateChanged();
         }
      });
   }
}
