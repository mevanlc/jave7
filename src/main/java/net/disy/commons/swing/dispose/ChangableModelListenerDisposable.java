package net.disy.commons.swing.dispose;

import net.disy.commons.core.model.IChangeableModel;
import net.disy.commons.core.model.listener.IChangeListener;

public class ChangableModelListenerDisposable implements IDisposable {
   private final IChangeableModel model;
   private final IChangeListener listener;

   public ChangableModelListenerDisposable(IChangeableModel model, IChangeListener listener) {
      this.model = model;
      this.listener = listener;
   }

   @Override
   public void dispose() {
      this.model.removeChangeListener(this.listener);
   }
}
