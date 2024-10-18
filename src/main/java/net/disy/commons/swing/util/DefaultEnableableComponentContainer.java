package net.disy.commons.swing.util;

import javax.swing.JComponent;

public class DefaultEnableableComponentContainer implements IEnableableComponentContainer {
   private final JComponent panel;

   public DefaultEnableableComponentContainer(JComponent panel) {
      this.panel = panel;
      GuiUtilities.setContainerEnabled(panel, this.isEnabled());
   }

   public boolean isEnabled() {
      return this.panel.isEnabled();
   }

   @Override
   public JComponent[] getComponents() {
      return new JComponent[]{this.panel};
   }

   @Override
   public void setEnabled(boolean enabled) {
      GuiUtilities.setContainerEnabled(this.panel, enabled);
   }
}
