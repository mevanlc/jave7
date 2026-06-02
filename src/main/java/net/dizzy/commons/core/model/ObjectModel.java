package net.dizzy.commons.core.model;

import java.util.Objects;

public class ObjectModel<T> extends AbstractChangeableModel {
   private T value;

   public ObjectModel() {
      this(null);
   }

   public ObjectModel(T value) {
      this.value = value;
   }

   public T getValue() {
      return value;
   }

   public void setValue(T value) {
      if (!Objects.equals(this.value, value)) {
         this.value = value;
         fireChangeEvent();
      }
   }
}
