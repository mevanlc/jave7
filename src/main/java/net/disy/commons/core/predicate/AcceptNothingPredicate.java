package net.disy.commons.core.predicate;

public class AcceptNothingPredicate<T> implements IPredicate<T> {
   @Override
   public boolean evaluate(T value) {
      return false;
   }
}
