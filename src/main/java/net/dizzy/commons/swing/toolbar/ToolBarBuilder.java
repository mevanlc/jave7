package net.dizzy.commons.swing.toolbar;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JToolBar;

public class ToolBarBuilder {
   private final JToolBar toolBar = new JToolBar();

   public ToolBarBuilder add(Action action) {
      toolBar.add(action);
      return this;
   }

   public ToolBarBuilder add(JComponent component) {
      toolBar.add(component);
      return this;
   }

   public JToolBar createToolBar() {
      return toolBar;
   }
}
