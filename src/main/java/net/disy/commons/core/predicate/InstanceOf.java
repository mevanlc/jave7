package net.disy.commons.core.predicate;

import net.disy.commons.core.util.Ensure;

public final class InstanceOf<T> implements IPredicate<T> {
   private final Class<?> clazz;

   public InstanceOf(Class<?> clazz) {
      Ensure.ensureArgumentNotNull(clazz);
      this.clazz = clazz;
   }

   @Override
   public boolean evaluate(T value) {
      return this.clazz.isInstance(value);
   }
}
