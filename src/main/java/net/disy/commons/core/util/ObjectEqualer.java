package net.disy.commons.core.util;

public class ObjectEqualer<T> {
   public boolean equals(T o1, T o2) {
      return ObjectUtilities.equals(o1, o2);
   }
}
