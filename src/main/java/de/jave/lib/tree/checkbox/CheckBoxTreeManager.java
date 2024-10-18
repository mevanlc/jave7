package de.jave.lib.tree.checkbox;

import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;

public class CheckBoxTreeManager {
   private final ICheckBoxNodeAccess access;
   private CheckBoxSelectionMode mode = CheckBoxSelectionMode.SINGLE_SELECTION;
   private final TreeCellRenderer defaultRenderer;

   public CheckBoxTreeManager(ICheckBoxNodeAccess access, CheckBoxSelectionMode mode, TreeCellRenderer defaultRenderer) {
      this.defaultRenderer = defaultRenderer;
      this.mode = mode;
      this.access = access;
   }

   public TreeCellRenderer createTreeRenderer() {
      return new CheckboxTreeRenderer(this.access, this.defaultRenderer);
   }

   public TreeCellEditor createTreeEditor() {
      return new CheckboxTreeEditor(this.access, this.mode, this.defaultRenderer);
   }
}
