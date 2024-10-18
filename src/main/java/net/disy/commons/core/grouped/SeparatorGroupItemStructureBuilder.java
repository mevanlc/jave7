package net.disy.commons.core.grouped;

public class SeparatorGroupItemStructureBuilder<G, I> extends GroupItemStructureBuilder<G, I> implements IGroupItemStructureBuilder<I> {
   public SeparatorGroupItemStructureBuilder() {
   }

   public SeparatorGroupItemStructureBuilder(IGroupHandler<G> groupHandler) {
      super(groupHandler);
   }

   @Override
   public void addAllItemsTo(IStructuredItemAddable<I> addable) {
      boolean addSeparator = false;

      for (G group : this.getGroups()) {
         if (addSeparator) {
            addable.addSeparator();
         }

         addSeparator = false;

         for (I item : this.getGroupItems(group)) {
            addable.add(item);
            addSeparator = true;
         }
      }
   }
}
