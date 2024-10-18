package net.disy.commons.swing.util;

import java.awt.Component;

public class ComponentEnableableAdapter implements IEnableable {
   private final Component component;

   public ComponentEnableableAdapter(Component component) {
      this.component = component;
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.component.setEnabled(enabled);
   }
}
