package net.disy.commons.swing.component;

import java.awt.BorderLayout;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.swing.toolbar.ToolBarUtilities;

public class ButtonizedComponent implements IComponentContainer {
   private final JPanel panel;
   private final AbstractButton button;
   private final JComponent component;

   public ButtonizedComponent(Action action, JComponent component) {
      this.component = component;
      this.panel = new JPanel(new BorderLayout(0, 0));
      this.button = ToolBarUtilities.createToolBarButton(action);
      this.panel.add(this.button, "West");
      this.panel.add(component, "Center");
   }

   @Override
   public JComponent getContent() {
      return this.panel;
   }

   public void setEnabled(boolean enabled) {
      this.button.setEnabled(enabled);
      this.component.setEnabled(enabled);
   }

   public void setButtonVisible(boolean visible) {
      this.button.setVisible(visible);
   }
}
