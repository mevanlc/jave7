package net.disy.commons.core.collection;

import java.util.AbstractList;
import net.disy.commons.core.util.Ensure;

public class ConstantList<T> extends AbstractList<T> {
   private final T[] elements;

   public ConstantList(T... elements) {
      Ensure.ensureArgumentNotNull(elements);
      this.elements = elements;
   }

   @Override
   public T get(int index) {
      return this.elements[index];
   }

   @Override
   public int size() {
      return this.elements.length;
   }
}
