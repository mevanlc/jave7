package net.disy.commons.swing.table;

import javax.swing.table.AbstractTableModel;
import net.disy.commons.core.model.IChangeableModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;

public abstract class SmartTableModel extends AbstractTableModel {
   private final ITableModelColumnSettings[] settings;

   public SmartTableModel(ITableModelColumnSettings[] settings, IChangeableModel... models) {
      Ensure.ensureArgumentNotNull(settings);
      this.settings = settings;

      for (IChangeableModel model : models) {
         model.addChangeListener(new IChangeListener() {
            @Override
            public void stateChanged() {
               SmartTableModel.this.fireTableDataChanged();
            }
         });
      }
   }

   @Override
   public final int getColumnCount() {
      return this.settings.length;
   }

   @Override
   public final Object getValueAt(int rowIndex, int columnIndex) {
      return this.settings[columnIndex].getValueAt(rowIndex);
   }

   @Override
   public final String getColumnName(int column) {
      return this.settings[column].getColumnName();
   }

   @Override
   public final void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      this.settings[columnIndex].setValueAt(aValue, rowIndex);
   }

   @Override
   public final Class<?> getColumnClass(int columnIndex) {
      return this.settings[columnIndex].getItemClass();
   }

   @Override
   public final boolean isCellEditable(int rowIndex, int columnIndex) {
      return this.settings[columnIndex].isCellEditable(rowIndex);
   }
}
