package net.disy.commons.swing.smarttable.column;

import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;
import net.disy.commons.swing.smarttable.SmartTable;
import net.disy.commons.swing.smarttable.SmartTableFactory;
import net.disy.commons.swing.smarttable.actions.ITableActionFactory;

public class RowObjectTable<T> {
   private final SmartTable smartTable;
   private final DefaultTableModel tableModel;

   public RowObjectTable(T[] rowObjects, ITableColumn<T>... columns) {
      this.smartTable = SmartTableFactory.create(columns);
      this.tableModel = (DefaultTableModel)this.smartTable.getModel();
      this.update(rowObjects);
   }

   public final void addActionFactory(ITableActionFactory actionFactory) {
      this.smartTable.addActionFactory(actionFactory);
   }

   public final JComponent getContent() {
      return this.smartTable.getContent();
   }

   public final void update(T[] rowObjects) {
      int currentRowCount = this.tableModel.getRowCount();
      int newItemCount = rowObjects.length;
      int regularRows = Math.min(newItemCount, currentRowCount);

      for (int rowIndex = 0; rowIndex < regularRows; rowIndex++) {
         for (int columnIndex = 0; columnIndex < this.tableModel.getColumnCount(); columnIndex++) {
            T value = rowObjects[rowIndex];
            this.tableModel.setValueAt(value, rowIndex, columnIndex);
         }
      }

      for (int surplusRowIndex = currentRowCount - 1; surplusRowIndex >= newItemCount; surplusRowIndex--) {
         this.tableModel.removeRow(surplusRowIndex);
      }

      for (int surplusItemIndex = currentRowCount; surplusItemIndex < newItemCount; surplusItemIndex++) {
         Object[] row = new Object[this.tableModel.getColumnCount()];
         Arrays.fill(row, rowObjects[surplusItemIndex]);
         this.tableModel.addRow(row);
      }
   }
}
