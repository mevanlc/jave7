package net.disy.commons.swing.toolbar;

import javax.swing.Action;
import net.disy.commons.core.grouped.IGroupedItem;

public class GroupedToolBarItem implements IGroupedItem<String, Action> {
   private final String groupId;
   private final Action action;

   public GroupedToolBarItem(String groupId, Action action) {
      this.groupId = groupId;
      this.action = action;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public Action getItem() {
      return this.action;
   }
}
