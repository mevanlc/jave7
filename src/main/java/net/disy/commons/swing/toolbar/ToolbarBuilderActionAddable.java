package net.disy.commons.swing.toolbar;

import javax.swing.Action;
import net.disy.commons.core.grouped.IStructuredItemAddable;

public class ToolbarBuilderActionAddable implements IStructuredItemAddable<Action> {
   private final ToolBarBuilder builder;

   public ToolbarBuilderActionAddable(ToolBarBuilder builder) {
      this.builder = builder;
   }

   @Override
   public void addSeparator() {
      this.builder.addSeparator();
   }

   public void add(Action item) {
      this.builder.add(item);
   }
}
