package net.disy.commons.swing.smarttable.actions;

import java.awt.Component;
import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.smarttable.SmartTable;

public final class AddRowTableAction extends SmartAction {
   private final SmartTable table;
   private final IAdditionPerformer additionPerformer;

   public AddRowTableAction(IActionConfiguration configuration, SmartTable table, IAdditionPerformer additionPerformer) {
      super(configuration);
      this.table = table;
      this.additionPerformer = additionPerformer;
   }

   @Override
   protected void execute(Component parentComponent) {
      this.table.stopCellEditing();
      if (this.additionPerformer.performAdd(parentComponent)) {
         this.table.scrollToAndSelect(this.table.getTable().getModel().getRowCount() - 1);
         this.table.requestFocus();
      }
   }
}
