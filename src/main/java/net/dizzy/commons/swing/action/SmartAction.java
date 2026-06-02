package net.dizzy.commons.swing.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;

public abstract class SmartAction extends AbstractAction {
   public SmartAction() {
      super();
   }

   public SmartAction(String name) {
      super(name);
   }

   public SmartAction(String name, Icon icon) {
      super(name, icon);
   }

   public SmartAction(Icon icon) {
      super(null, icon);
   }

   @Override
   public final void actionPerformed(ActionEvent event) {
      Object source = event.getSource();
      execute(source instanceof Component ? (Component) source : null);
   }

   protected abstract void execute(Component parent);

   public void setToolTipText(String toolTipText) {
      putValue(SHORT_DESCRIPTION, toolTipText);
   }

   public void setAcceleratorKey(KeyStroke acceleratorKey) {
      putValue(ACCELERATOR_KEY, acceleratorKey);
   }

   public void setIcon(Icon icon) {
      putValue(SMALL_ICON, icon);
   }

   public void setName(String name) {
      putValue(NAME, name);
   }

   public Icon getIcon() {
      Object value = getValue(SMALL_ICON);
      return value instanceof Icon ? (Icon) value : null;
   }
}
