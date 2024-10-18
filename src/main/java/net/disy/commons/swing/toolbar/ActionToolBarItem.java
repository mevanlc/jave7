package net.disy.commons.swing.toolbar;

import javax.swing.Action;

public class ActionToolBarItem implements IToolBarItem {
   private final Action action;

   public ActionToolBarItem(Action action) {
      this.action = action;
   }

   @Override
   public void addTo(ToolBarBuilder toolBarBuilder) {
      toolBarBuilder.add(this.action);
   }
}
