package net.disy.commons.core.util;

import net.disy.commons.core.predicate.IPredicate;

public class NonNullPredicate<T> implements IPredicate<T> {
   @Override
   public boolean evaluate(T value) {
      return value != null;
   }
}
