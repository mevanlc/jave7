package net.disy.commons.swing.dialog.input;

import net.disy.commons.core.model.listener.ListenerList;
import net.disy.commons.core.util.IClosure;

public abstract class AbstractSmartDialogPanel implements ISmartDialogPanel {
   private final transient ListenerList<IRequestFinishListener> listeners = new ListenerList<>();

   @Override
   public final void addRequestFinishListener(IRequestFinishListener listener) {
      this.listeners.add(listener);
   }

   public final void removeRequestFinishListener(IRequestFinishListener listener) {
      this.listeners.remove(listener);
   }

   protected final void fireRequestFinish() {
      this.listeners.forAllDo(new IClosure<IRequestFinishListener>() {
         public void execute(IRequestFinishListener listener) {
            listener.requestFinish();
         }
      });
   }
}
