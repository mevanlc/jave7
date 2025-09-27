package net.disy.commons.swing.smarttable.actions;

import java.awt.Component;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.smarttable.SmartTable;

public final class RemoveRowTableAction extends SmartAction {
   private final SmartTable table;
   private final ITableRowRemovePerformer removePerformer;

   public RemoveRowTableAction(IActionConfiguration configuration, SmartTable table, ITableRowRemovePerformer removePerformer) {
      super(configuration);
      this.table = table;
      this.removePerformer = removePerformer;
      table.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            RemoveRowTableAction.this.updateEnabled();
         }
      });
      this.updateEnabled();
   }

   private void updateEnabled() {
      this.setEnabled(!this.table.isSelectionEmpty());
   }

   @Override
   protected void execute(Component parentComponent) {
      int selectedRowIndex = this.table.getTable().getSelectedRow();
      this.table.stopCellEditing();
      if (this.removePerformer.performRemove(parentComponent, selectedRowIndex) && this.table.getTable().getModel().getRowCount() > 0) {
         int newRowIndex = Math.min(this.table.getTable().getModel().getRowCount() - 1, selectedRowIndex);
         this.table.scrollToAndSelect(newRowIndex);
         this.table.requestFocus();
      }
   }
}
