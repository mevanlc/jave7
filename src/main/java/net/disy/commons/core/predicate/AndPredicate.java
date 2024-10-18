package net.disy.commons.core.predicate;

import java.util.ArrayList;
import java.util.List;

public class AndPredicate<T> implements IPredicate<T> {
   private final List<IPredicate<T>> allPredicates = new ArrayList<>();

   public AndPredicate(IPredicate<T>... predicates) {
      for (IPredicate<T> predicate : predicates) {
         this.addPredicate(predicate);
      }
   }

   @Override
   public boolean evaluate(T value) {
      for (IPredicate<T> predicate : this.allPredicates) {
         if (!predicate.evaluate(value)) {
            return false;
         }
      }

      return true;
   }

   public void addPredicate(IPredicate<T> predicate) {
      this.allPredicates.add(predicate);
   }
}
