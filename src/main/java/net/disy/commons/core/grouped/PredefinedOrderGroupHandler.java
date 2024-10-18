package net.disy.commons.core.grouped;

import java.util.ArrayList;
import java.util.Arrays;
import net.disy.commons.core.util.Ensure;

public class PredefinedOrderGroupHandler<T> implements IGroupHandler<T> {
   private final ArrayList<T> allGroups = new ArrayList<>();
   private T beforeInsertPosition;

   public PredefinedOrderGroupHandler(T... predefinedGroups) {
      this.allGroups.addAll(Arrays.asList(predefinedGroups));
   }

   @Override
   public void addGroup(T groupId) {
      if (!this.allGroups.contains(groupId)) {
         if (this.beforeInsertPosition == null) {
            this.allGroups.add(groupId);
         } else {
            this.allGroups.add(this.allGroups.indexOf(this.beforeInsertPosition), groupId);
         }
      }
   }

   @Override
   public Iterable<T> getGroupsInDisplayOrder() {
      return this.allGroups;
   }

   public void setGroupInsertPositionBefore(T beforeInsertPosition) {
      Ensure.ensureArgumentTrue("unknown group: " + beforeInsertPosition, this.allGroups.contains(beforeInsertPosition));
      this.beforeInsertPosition = beforeInsertPosition;
   }
}
