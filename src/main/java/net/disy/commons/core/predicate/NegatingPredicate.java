package net.disy.commons.core.predicate;

import net.disy.commons.core.util.Ensure;

public class NegatingPredicate<T> implements IPredicate<T> {
   private final IPredicate<T> predicate;

   public NegatingPredicate(IPredicate<T> predicate) {
      Ensure.ensureArgumentNotNull(predicate);
      this.predicate = predicate;
   }

   @Override
   public boolean evaluate(T value) {
      return !this.predicate.evaluate(value);
   }
}
