package net.disy.commons.swing.smarttable.actions;

import java.awt.Component;
import javax.swing.Action;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.smarttable.SmartTable;

public abstract class MoveRowDownTableActionFactory implements ITableActionFactory {
   @Override
   public Action createAction(final SmartTable table) {
      final SmartAction action = new SmartAction("Nach unten") {
         @Override
         protected void execute(Component parentComponent) {
            int rowIndex = table.getTable().getSelectedRow();
            table.stopCellEditing();
            boolean success = MoveRowDownTableActionFactory.this.performMoveDown(rowIndex);
            if (success) {
               table.scrollToAndSelect(rowIndex + 1);
               table.requestFocus();
            }
         }
      };
      table.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            MoveRowDownTableActionFactory.this.updateEnabled(action, table);
         }
      });
      this.updateEnabled(action, table);
      return action;
   }

   private void updateEnabled(Action action, SmartTable smartTable) {
      int rowIndex = smartTable.getSelectedRowIndex();
      boolean hasSelection = rowIndex != -1;
      boolean lastRowSelected = rowIndex == smartTable.getTable().getRowCount() - 1;
      action.setEnabled(hasSelection && !lastRowSelected);
   }

   protected abstract boolean performMoveDown(int var1);
}
