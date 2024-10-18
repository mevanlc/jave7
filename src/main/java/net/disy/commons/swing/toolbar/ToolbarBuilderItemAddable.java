package net.disy.commons.swing.toolbar;

import net.disy.commons.core.grouped.IStructuredItemAddable;

public class ToolbarBuilderItemAddable implements IStructuredItemAddable<IToolBarItem> {
   private final ToolBarBuilder builder;

   public ToolbarBuilderItemAddable(ToolBarBuilder builder) {
      this.builder = builder;
   }

   @Override
   public void addSeparator() {
      this.builder.addSeparator();
   }

   public void add(IToolBarItem item) {
      this.builder.add(item);
   }
}
