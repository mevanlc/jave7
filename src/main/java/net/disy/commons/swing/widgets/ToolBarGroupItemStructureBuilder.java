package net.disy.commons.swing.toolbar;

import javax.swing.Action;
import javax.swing.JComponent;
import net.disy.commons.core.grouped.IGroupHandler;
import net.disy.commons.core.grouped.SeparatorGroupItemStructureBuilder;

public class ToolBarGroupItemStructureBuilder<G> extends SeparatorGroupItemStructureBuilder<G, IToolBarItem> {
   public ToolBarGroupItemStructureBuilder() {
   }

   public ToolBarGroupItemStructureBuilder(IGroupHandler<G> groupHandler) {
      super(groupHandler);
   }

   public void add(G groupId, Action action) {
      this.add(groupId, new ActionToolBarItem(action));
   }

   public void add(G groupId, JComponent component) {
      this.add(groupId, new ComponentToolBarItem(component));
   }
}
