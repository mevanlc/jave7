package net.disy.commons.swing.update;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class UpdatingPropertyChangeListener implements PropertyChangeListener {
   private final IUpdatable updatable;

   public UpdatingPropertyChangeListener(IUpdatable updatable) {
      this.updatable = updatable;
   }

   @Override
   public void propertyChange(PropertyChangeEvent evt) {
      this.updatable.update();
   }
}
