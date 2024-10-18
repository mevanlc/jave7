package net.disy.commons.swing.panel;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public class VerticalScrollableComponentContainer {
   private final JComponent content;
   private final VerticalScrollableComponentPanel panel;

   public VerticalScrollableComponentContainer(int preferredViewportHeight) {
      this.panel = new VerticalScrollableComponentPanel(preferredViewportHeight);
      this.content = new JScrollPane(this.panel);
   }

   public void addComponent(JComponent component) {
      this.panel.add(component, GridDialogLayoutData.FILL_HORIZONTAL);
   }

   public JComponent getContent() {
      return this.content;
   }
}
