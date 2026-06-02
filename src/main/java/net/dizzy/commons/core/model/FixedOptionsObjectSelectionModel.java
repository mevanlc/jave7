package net.dizzy.commons.core.model;

import java.util.Arrays;

public class FixedOptionsObjectSelectionModel<T> extends AbstractChangeableModel {
   private final T[] values;
   private T selectedValue;

   public FixedOptionsObjectSelectionModel(T[] values) {
      this.values = values;
      if (values.length > 0) {
         selectedValue = values[0];
      }
   }

   public void setSelectedValue(T selectedValue) {
      if (!java.util.Objects.equals(this.selectedValue, selectedValue)) {
         this.selectedValue = selectedValue;
         fireChangeEvent();
      }
   }

   public T[] getAllValues() {
      return Arrays.copyOf(values, values.length);
   }

   public T getFirstSelectedValue() {
      return selectedValue;
   }
}
