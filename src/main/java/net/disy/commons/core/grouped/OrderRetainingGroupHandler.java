package net.disy.commons.core.grouped;

import java.util.LinkedHashSet;
import java.util.Set;

public class OrderRetainingGroupHandler<G> implements IGroupHandler<G> {
   private final Set<G> groups = new LinkedHashSet<>();

   @Override
   public void addGroup(G group) {
      this.groups.add(group);
   }

   @Override
   public Iterable<G> getGroupsInDisplayOrder() {
      return this.groups;
   }
}
