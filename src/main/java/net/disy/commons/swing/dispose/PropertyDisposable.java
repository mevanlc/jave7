package net.disy.commons.swing.dispose;

import java.beans.PropertyChangeListener;
import net.disy.commons.swing.events.IPropertyControl;

public class PropertyDisposable implements IDisposable {
   private final IPropertyControl properties;
   private final PropertyChangeListener listener;

   public PropertyDisposable(IPropertyControl properties, PropertyChangeListener listener) {
      this.properties = properties;
      this.listener = listener;
   }

   @Override
   public void dispose() {
      this.properties.removePropertyChangeListener(this.listener);
   }
}
