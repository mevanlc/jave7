package net.disy.commons.core.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ObjectSelectionModel<T> extends AbstractChangeableModel {
   private final Set<T> selectedValues = new HashSet<>();

   public final void setSelected(T value, boolean selected) {
      if (selected != this.isSelected(value)) {
         if (selected) {
            this.selectedValues.add(value);
         } else {
            this.selectedValues.remove(value);
         }

         this.fireChangeEvent();
      }
   }

   public final boolean isSelected(T value) {
      return this.selectedValues.contains(value);
   }

   protected Set<T> getSelectedValuesSet() {
      return this.selectedValues;
   }

   public boolean isEmpty() {
      return this.selectedValues.isEmpty();
   }

   public Set<T> getAsSet() {
      return Collections.unmodifiableSet(this.getSelectedValuesSet());
   }

   public void clear() {
      this.selectedValues.clear();
      this.fireChangeEvent();
   }
}
