package net.dizzy.commons.swing.dialog.input.select;

import java.awt.Component;

@SuppressWarnings("unchecked")
public final class SmartSomeOutOfManySelectionDialog {
   private SmartSomeOutOfManySelectionDialog() {
   }

   public static <T> ISomeOutOfManyDialogResult<T> showSelectionDialog(Component parent, ISomeOutOfManyDialogPanelConfiguration<T> configuration) {
      return new Result<>(false, configuration.getValues());
   }

   public static <T> ISomeOutOfManyDialogResult<T> showSelectOneOutOfManyDialog(Component parent, AbstractOneOutOfManyDialogConfiguration<T> configuration) {
      T[] values = configuration.getValues();
      T[] selected = values == null || values.length == 0 ? values : java.util.Arrays.copyOf(values, 1);
      return new Result<>(false, selected);
   }

   private static class Result<T> implements ISomeOutOfManyDialogResult<T> {
      private final boolean canceled;
      private final T[] values;
      Result(boolean canceled, T[] values) { this.canceled = canceled; this.values = values; }
      @Override public boolean isCanceled() { return canceled; }
      @Override public T[] getSelectedValues() { return values; }
   }
}
