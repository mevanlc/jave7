package net.disy.commons.swing.update;

import net.disy.commons.core.model.listener.IChangeListener;

public class UpdateChangeListener implements IChangeListener {
   private final IUpdatable updatable;

   public UpdateChangeListener(IUpdatable updatable) {
      this.updatable = updatable;
   }

   @Override
   public void stateChanged() {
      this.updatable.update();
   }
}
