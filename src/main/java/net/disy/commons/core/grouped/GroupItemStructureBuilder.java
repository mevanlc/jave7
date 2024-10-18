package net.disy.commons.core.grouped;

import net.disy.commons.core.collection.MultipleValueHashMap;
import net.disy.commons.core.util.Ensure;

public class GroupItemStructureBuilder<G, I> {
   private final MultipleValueHashMap<G, I> itemsByGroupId = new MultipleValueHashMap<>();
   private final IGroupHandler<G> groupHandler;

   public GroupItemStructureBuilder() {
      this(new OrderRetainingGroupHandler<>());
   }

   public GroupItemStructureBuilder(IGroupHandler<G> groupHandler) {
      this.groupHandler = groupHandler;
   }

   public Iterable<I> getGroupItems(G group) {
      return this.itemsByGroupId.getList(group);
   }

   public Iterable<G> getGroups() {
      return this.groupHandler.getGroupsInDisplayOrder();
   }

   public void add(IGroupedItem<G, I> menuItem) {
      this.add(menuItem.getGroupId(), menuItem.getItem());
   }

   public void add(G groupId, I item) {
      Ensure.ensureArgumentNotNull(item);
      Ensure.ensureArgumentNotNull(groupId);
      this.groupHandler.addGroup(groupId);
      this.itemsByGroupId.add(groupId, item);
   }

   public boolean isEmpty() {
      return this.itemsByGroupId.isEmpty();
   }
}
