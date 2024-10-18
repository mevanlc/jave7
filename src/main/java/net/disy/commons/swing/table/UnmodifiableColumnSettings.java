package net.disy.commons.swing.table;

import net.disy.commons.core.util.Ensure;

public class UnmodifiableColumnSettings<T> implements ITableModelColumnSettings<T> {
   private final T[] objects;
   private final Class<T> itemClass;
   private final String columnName;

   public UnmodifiableColumnSettings(T[] objects, String columnName, Class<T> itemClass) {
      Ensure.ensureArgumentNotNull(objects);
      Ensure.ensureArgumentNotNull(columnName);
      Ensure.ensureArgumentNotNull(itemClass);
      this.objects = objects;
      this.columnName = columnName;
      this.itemClass = itemClass;
   }

   @Override
   public String getColumnName() {
      return this.columnName;
   }

   @Override
   public Class<T> getItemClass() {
      return this.itemClass;
   }

   @Override
   public T getValueAt(int rowIndex) {
      return this.objects[rowIndex];
   }

   @Override
   public void setValueAt(T value, int rowIndex) {
   }

   @Override
   public boolean isCellEditable(int rowIndex) {
      return false;
   }
}
