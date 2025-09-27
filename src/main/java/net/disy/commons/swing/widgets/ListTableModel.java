package net.disy.commons.swing.table;

import javax.swing.table.AbstractTableModel;
import net.disy.commons.core.list.IListModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.util.GuiUtilities;

public class ListTableModel<T> extends AbstractTableModel {
   private final IListModel<T> listModel;
   private final String[] columnNames;

   public ListTableModel(IListModel<T> listModel, String... columnNames) {
      Ensure.ensureArgumentNotNull(listModel);
      Ensure.ensureArgumentNotNull(columnNames);
      this.columnNames = columnNames;
      this.listModel = listModel;
      listModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            GuiUtilities.invokeLaterIfNecessary(new Runnable() {
               @Override
               public void run() {
                  ListTableModel.this.fireTableDataChanged();
               }
            });
         }
      });
   }

   @Override
   public int getColumnCount() {
      return this.columnNames.length;
   }

   @Override
   public String getColumnName(int column) {
      return this.columnNames[column];
   }

   @Override
   public int getRowCount() {
      return this.listModel.getItemCount();
   }

   @Override
   public T getValueAt(int rowIndex, int columnIndex) {
      return this.listModel.getItem(rowIndex);
   }
}
