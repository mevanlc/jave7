package de.jave.jave.clipart;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import net.disy.commons.core.util.Ensure;

public class ClipartGroupListModel extends AbstractListModel {
   private final ClipartManager clipartManager;
   private final List<ClipartGroup> temporaryGroups = new ArrayList<>();

   public ClipartGroupListModel(ClipartManager clipartManager) {
      Ensure.ensureArgumentNotNull(clipartManager);
      this.clipartManager = clipartManager;
   }

   public ClipartGroupItem getElementAt(int index) {
      return index < this.clipartManager.getGroupCount()
         ? new ClipartGroupItem(this.clipartManager.getGroup(index), true)
         : new ClipartGroupItem(this.temporaryGroups.get(index - this.clipartManager.getGroupCount()), false);
   }

   @Override
   public int getSize() {
      return this.clipartManager.getGroupCount() + this.temporaryGroups.size();
   }

   public void add(ClipartGroup newGroup) {
      this.temporaryGroups.add(newGroup);
      this.fireIntervalAdded(newGroup, this.getSize() - 1, this.getSize() - 1);
   }

   public int getItemIndex(ClipartGroup value) {
      for (int index = 0; index < this.clipartManager.getGroupCount(); index++) {
         ClipartGroup group = this.clipartManager.getGroup(index);
         if (group.equals(value)) {
            return index;
         }
      }

      for (int i = 0; i < this.temporaryGroups.size(); i++) {
         if (this.temporaryGroups.get(i).equals(value)) {
            return this.clipartManager.getGroupCount() + i;
         }
      }

      return -1;
   }
}
