package net.disy.commons.swing.toolbar;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JToolBar;

public class ToolBarBuilder {
   private final JToolBar toolBar;

   public ToolBarBuilder() {
      this(new ToolBarConfiguration());
   }

   public ToolBarBuilder(boolean floatable) {
      this(new ToolBarConfiguration(floatable));
   }

   @Deprecated
   public ToolBarBuilder(JToolBar toolbar) {
      this.toolBar = toolbar;
   }

   public ToolBarBuilder(IToolBarConfiguration configuration) {
      this(ToolBarUtilities.createEmptyToolBar(configuration));
   }

   public ToolBarBuilder add(Action action) {
      ToolBarUtilities.addToolBarButton(this.toolBar, action);
      return this;
   }

   public ToolBarBuilder add(Action[] actions) {
      for (int i = 0; i < actions.length; i++) {
         this.add(actions[i]);
      }

      return this;
   }

   public ToolBarBuilder add(Component... components) {
      for (Component component : components) {
         if (component instanceof AbstractButton) {
            ToolBarUtilities.configureToolBarButton((AbstractButton)component);
         }

         this.toolBar.add(component);
      }

      return this;
   }

   public ToolBarBuilder add(IToolBarItem item) {
      item.addTo(this);
      return this;
   }

   public ToolBarBuilder addSeparator() {
      this.toolBar.addSeparator();
      return this;
   }

   public ToolBarBuilder addSeparator(Dimension size) {
      this.toolBar.addSeparator(size);
      return this;
   }

   @Deprecated
   public JToolBar getToolBar() {
      return this.toolBar;
   }

   public JToolBar createToolBar() {
      return this.toolBar;
   }
}
