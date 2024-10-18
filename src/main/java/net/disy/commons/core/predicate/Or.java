package net.disy.commons.core.predicate;

import net.disy.commons.core.util.Ensure;

public class Or<T> implements IPredicate<T> {
   private final IPredicate<T> first;
   private final IPredicate<T> second;

   public static <T> Or<T> For(IPredicate<T> first, IPredicate<T> second) {
      return new Or<>(first, second);
   }

   public Or(IPredicate<T> first, IPredicate<T> second) {
      Ensure.ensureArgumentNotNull(first);
      Ensure.ensureArgumentNotNull(second);
      this.second = second;
      this.first = first;
   }

   @Override
   public boolean evaluate(T value) {
      return this.first.evaluate(value) || this.second.equals(value);
   }
}
