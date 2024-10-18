package net.disy.commons.swing.toolbar;

import javax.swing.AbstractButton;
import javax.swing.Action;
import net.disy.commons.core.grouped.IGroupedItem;

public class ToolbarActionContainer implements IGroupedActionContainer {
   private final ToolBarBuilder builder;

   public ToolbarActionContainer(ToolBarBuilder builder) {
      this.builder = builder;
   }

   @Override
   public IGroupedActionContainer add(IGroupedItem<String, Action> item) {
      this.builder.add(item.getItem());
      return this;
   }

   @Override
   public IGroupedActionContainer add(String groupId, Action action) {
      this.builder.add(action);
      return this;
   }

   @Override
   public IGroupedActionContainer add(String groupId, AbstractButton button) {
      this.builder.add(button);
      return this;
   }
}
