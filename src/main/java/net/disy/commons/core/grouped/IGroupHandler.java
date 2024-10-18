package net.disy.commons.core.grouped;

public interface IGroupHandler<G> {
   void addGroup(G var1);

   Iterable<G> getGroupsInDisplayOrder();
}
