package net.disy.commons.core.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.disy.commons.core.predicate.IPredicate;
import net.disy.commons.core.util.ArrayUtilities;

public class FixedOptionsObjectSelectionModel<T> extends ObjectSelectionModel<T> {
   private final T[] allValues;

   public FixedOptionsObjectSelectionModel(T[] allValues) {
      this.allValues = allValues;
   }

   public void setSelectedValue(T value) {
      if (value == null) {
         this.clearSelection();
      } else {
         Set<T> selectedValuesSet = this.getSelectedValuesSet();
         if (selectedValuesSet.size() != 1 || !selectedValuesSet.contains(value)) {
            selectedValuesSet.clear();
            selectedValuesSet.add(value);
            this.fireChangeEvent();
         }
      }
   }

   public void clearSelection() {
      Set<T> selectedValuesSet = this.getSelectedValuesSet();
      if (selectedValuesSet.size() != 0) {
         this.getSelectedValuesSet().clear();
         this.fireChangeEvent();
      }
   }

   public void setSelectedValues(T[] values) {
      this.setSelectedValues(new HashSet<>(Arrays.asList(values)));
   }

   public void setSelectedValues(Set<T> newValues) {
      if (!newValues.equals(this.getSelectedValuesSet())) {
         this.getSelectedValuesSet().clear();
         this.getSelectedValuesSet().addAll(newValues);
         this.fireChangeEvent();
      }
   }

   public T getFirstSelectedValue() {
      return ArrayUtilities.getFirst(this.allValues, this.getPredicate());
   }

   public T[] getSelectedValues() {
      return ArrayUtilities.filter(this.allValues, this.getPredicate());
   }

   private IPredicate<T> getPredicate() {
      return new IPredicate<T>() {
         @Override
         public boolean evaluate(T input) {
            return FixedOptionsObjectSelectionModel.this.getSelectedValuesSet().contains(input);
         }
      };
   }

   public int[] getSelectedIndices() {
      return ArrayUtilities.getIndices(this.getSelectedValues(), this.allValues);
   }

   public T[] getAllValues() {
      return this.allValues;
   }
}
