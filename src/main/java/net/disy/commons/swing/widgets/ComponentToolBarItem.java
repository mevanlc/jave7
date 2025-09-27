package net.disy.commons.swing.toolbar;

import javax.swing.JComponent;

public class ComponentToolBarItem implements IToolBarItem {
   private final JComponent component;

   public ComponentToolBarItem(JComponent component) {
      this.component = component;
   }

   @Override
   public void addTo(ToolBarBuilder toolBarBuilder) {
      toolBarBuilder.add(this.component);
   }
}
