package net.disy.commons.swing.util;

import java.awt.Container;

public class ContainerEnableableAdapter implements IEnableable {
   private final Container container;

   public ContainerEnableableAdapter(Container container) {
      this.container = container;
   }

   @Override
   public void setEnabled(boolean enabled) {
      GuiUtilities.setContainerEnabled(this.container, enabled);
   }
}
