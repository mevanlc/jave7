package net.disy.commons.core.predicate;

public final class AcceptAllPredicate<T> implements IPredicate<T> {
   @Override
   public boolean evaluate(T value) {
      return true;
   }
}
