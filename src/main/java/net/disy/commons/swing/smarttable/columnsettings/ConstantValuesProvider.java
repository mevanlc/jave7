package net.disy.commons.swing.smarttable.columnsettings;

public class ConstantValuesProvider<T> implements IComboBoxValuesProvider<T> {
   private final T[] values;

   public ConstantValuesProvider(T[] values) {
      this.values = values;
   }

   @Override
   public T[] getValues(int rowIndex) {
      return this.values;
   }
}
