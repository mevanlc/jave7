package net.disy.commons.swing.util;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

public class ComponentEnableableCollection implements IEnableable {
   private final List<IEnableable> allEnableables = new ArrayList<>();

   public void addComponent(Component component) {
      this.allEnableables.add(new ComponentEnableableAdapter(component));
   }

   public void addContainer(Container container) {
      this.allEnableables.add(new ContainerEnableableAdapter(container));
   }

   @Override
   public void setEnabled(boolean enabled) {
      for (IEnableable enableable : this.allEnableables) {
         enableable.setEnabled(enabled);
      }
   }
}
