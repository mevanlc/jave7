package net.disy.commons.swing.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.ObjectUtilities;

public class DisableableProxyAction extends AbstractAction implements IActionProxy {
   private final Action action;
   private boolean enabled = true;

   public DisableableProxyAction(Action action) {
      Ensure.ensureArgumentNotNull(action);
      this.action = action;
      action.addPropertyChangeListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            DisableableProxyAction.this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
         }
      });
   }

   @Override
   public boolean isEnabled() {
      return this.enabled && this.action.isEnabled();
   }

   @Override
   public void setEnabled(boolean enabled) {
      if (this.enabled != enabled) {
         this.enabled = enabled;
         this.firePropertyChange("enabled", !this.isEnabled(), this.isEnabled());
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      this.action.actionPerformed(e);
   }

   @Override
   public Object getValue(String key) {
      return this.action.getValue(key);
   }

   @Override
   public void putValue(String key, Object newValue) {
      this.action.putValue(key, newValue);
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof DisableableProxyAction)) {
         return false;
      } else {
         DisableableProxyAction other = (DisableableProxyAction)obj;
         return ObjectUtilities.equals(other.action, this.action) && this.enabled == other.enabled;
      }
   }

   @Override
   public int hashCode() {
      return ObjectUtilities.getHashCode(this.action);
   }

   @Override
   public Action getOriginalAction() {
      return this.action;
   }
}
