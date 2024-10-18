package net.disy.commons.core.model;

import net.disy.commons.core.util.ObjectUtilities;

public class DefaultProperty<T> implements SmartChangeableModel.IProperty<T> {
   private T value;

   @Override
   public T getValue() {
      return this.value;
   }

   @Override
   public void setValue(T value) {
      this.value = value;
   }

   @Override
   public boolean equals(Object object) {
      if (!(object instanceof DefaultProperty)) {
         return false;
      } else {
         DefaultProperty<?> other = (DefaultProperty<?>)object;
         return ObjectUtilities.equals(this.value, other.value);
      }
   }

   @Override
   public int hashCode() {
      return ObjectUtilities.getHashCode(this.value);
   }
}
