package net.dizzy.commons.swing.dialog.input.select;

public interface ISomeOutOfManyDialogResult<T> {
   boolean isCanceled();
   T[] getSelectedValues();
   default T getSelectedItem() {
      T[] values = getSelectedValues();
      return values == null || values.length == 0 ? null : values[0];
   }
}
