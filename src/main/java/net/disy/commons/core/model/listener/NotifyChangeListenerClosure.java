package net.disy.commons.core.model.listener;

import net.disy.commons.core.util.IClosure;

public final class NotifyChangeListenerClosure implements IClosure<IChangeListener> {
   public static final NotifyChangeListenerClosure INSTANCE = new NotifyChangeListenerClosure();

   public void execute(IChangeListener listener) {
      listener.stateChanged();
   }
}
