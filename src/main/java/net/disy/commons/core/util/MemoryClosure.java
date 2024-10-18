package net.disy.commons.core.util;

public class MemoryClosure<T> implements IClosure<T> {
   private T value;

   @Override
   public void execute(T transmission) {
      this.value = transmission;
   }

   public T getValue() {
      return this.value;
   }
}
