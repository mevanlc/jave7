package net.disy.commons.core.predicate;

import net.disy.commons.core.util.Ensure;

public class Not<T> implements IPredicate<T> {
   private final IPredicate<T> predicate;

   public static <T> Not<T> For(IPredicate<T> predicate) {
      return new Not<>(predicate);
   }

   public Not(IPredicate<T> predicate) {
      Ensure.ensureArgumentNotNull(predicate);
      this.predicate = predicate;
   }

   @Override
   public boolean evaluate(T value) {
      return !this.predicate.evaluate(value);
   }
}
