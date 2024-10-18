package net.disy.commons.core.grouped;

public interface IGroupedItem<G, I> {
   G getGroupId();

   I getItem();
}
