package net.disy.commons.core.grouped;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComparableGroupHandler<G extends Comparable<G>> implements IGroupHandler<G> {
   private final List<G> groups = new ArrayList<>();

   public void addGroup(G groupId) {
      if (!this.groups.contains(groupId)) {
         this.groups.add(groupId);
      }
   }

   @Override
   public Iterable<G> getGroupsInDisplayOrder() {
      Collections.sort(this.groups);
      return this.groups;
   }
}
