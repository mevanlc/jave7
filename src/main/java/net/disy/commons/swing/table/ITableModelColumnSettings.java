package net.disy.commons.swing.table;

public interface ITableModelColumnSettings<T> {
   String getColumnName();

   Class<T> getItemClass();

   T getValueAt(int var1);

   void setValueAt(T var1, int var2);

   boolean isCellEditable(int var1);
}
