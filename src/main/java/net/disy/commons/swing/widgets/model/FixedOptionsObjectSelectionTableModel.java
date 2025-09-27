package net.disy.commons.swing.smarttable.model;

import javax.swing.table.AbstractTableModel;
import net.disy.commons.core.model.FixedOptionsObjectSelectionModel;

public class FixedOptionsObjectSelectionTableModel<T> extends AbstractTableModel {
   private final String[] columnNames;
   private final FixedOptionsObjectSelectionModel<T> selectionModel;

   public FixedOptionsObjectSelectionTableModel(FixedOptionsObjectSelectionModel<T> selectionModel, String activityColumnName, String itemColumnName) {
      this.selectionModel = selectionModel;
      this.columnNames = new String[]{activityColumnName, itemColumnName};
   }

   @Override
   public int getColumnCount() {
      return 2;
   }

   @Override
   public String getColumnName(int column) {
      return this.columnNames[column];
   }

   @Override
   public int getRowCount() {
      return this.selectionModel.getAllValues().length;
   }

   @Override
   public Object getValueAt(int rowIndex, int columnIndex) {
      return columnIndex == 0 ? this.selectionModel.isSelected(this.getItemForRow(rowIndex)) : this.getItemForRow(rowIndex);
   }

   @Override
   public boolean isCellEditable(int rowIndex, int columnIndex) {
      return columnIndex == 0;
   }

   @Override
   public void setValueAt(Object value, int rowIndex, int columnIndex) {
      if (columnIndex == 0) {
         this.selectionModel.setSelected(this.getItemForRow(rowIndex), (Boolean)value);
      }
   }

   private T getItemForRow(int rowIndex) {
      return this.selectionModel.getAllValues()[rowIndex];
   }
}
