package net.disy.commons.swing.toolbar;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JToolBar;
import net.disy.commons.core.grouped.IGroupedItem;
import net.disy.commons.core.grouped.IStructuredItemAddable;
import net.disy.commons.core.grouped.SeparatorGroupItemStructureBuilder;
import net.disy.commons.core.util.Ensure;

public class GroupedToolBarBuilder implements IGroupedActionContainer {
   private final SeparatorGroupItemStructureBuilder<String, AbstractButton> structureBuilder = new SeparatorGroupItemStructureBuilder<>();
   private final IToolBarConfiguration configuration;

   public GroupedToolBarBuilder() {
      this(new ToolBarConfiguration());
   }

   public GroupedToolBarBuilder(IToolBarConfiguration configuration) {
      Ensure.ensureArgumentNotNull(configuration);
      this.configuration = configuration;
   }

   @Override
   public IGroupedActionContainer add(IGroupedItem<String, Action> item) {
      this.add(item.getGroupId(), item.getItem());
      return this;
   }

   public GroupedToolBarBuilder add(String groupId, Action action) {
      this.add(groupId, ToolBarUtilities.createToolBarButton(action));
      return this;
   }

   @Override
   public IGroupedActionContainer add(String groupId, AbstractButton button) {
      this.structureBuilder.add(groupId, button);
      return this;
   }

   @Deprecated
   public JToolBar getToolBar() {
      return this.createToolBar();
   }

   public JToolBar createToolBar() {
      final JToolBar toolBar = ToolBarUtilities.createEmptyToolBar(this.configuration);
      this.structureBuilder.addAllItemsTo(new IStructuredItemAddable<AbstractButton>() {
         public void add(AbstractButton item) {
            toolBar.add(item);
         }

         @Override
         public void addSeparator() {
            toolBar.addSeparator();
         }
      });
      return toolBar;
   }
}
