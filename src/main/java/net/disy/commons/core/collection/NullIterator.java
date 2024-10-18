package net.disy.commons.core.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class NullIterator<T> implements Iterator<T> {
   @Override
   public void remove() {
      throw new IllegalStateException();
   }

   @Override
   public boolean hasNext() {
      return false;
   }

   @Override
   public T next() {
      throw new NoSuchElementException();
   }
}
