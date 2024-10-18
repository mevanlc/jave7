package net.disy.commons.core.predicate;

import net.disy.commons.core.util.ObjectUtilities;

public class ObjectEqualsPredicate<T> implements IPredicate<T> {
   private final T expectedValue;

   public ObjectEqualsPredicate(T expectedValue) {
      this.expectedValue = expectedValue;
   }

   @Override
   public boolean evaluate(T value) {
      return ObjectUtilities.equals(this.expectedValue, value);
   }
}
