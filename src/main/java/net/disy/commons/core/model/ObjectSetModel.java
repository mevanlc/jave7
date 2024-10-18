package net.disy.commons.core.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ObjectSetModel<T> extends AbstractChangeableModel implements IObjectSetModel<T> {
   private final Set<T> set = Collections.synchronizedSet(new HashSet<>());

   @Override
   public void add(T... items) {
      if (this.set.addAll(Arrays.asList(items))) {
         this.fireChangeEvent();
      }
   }

   @Override
   public void clear() {
      if (!this.set.isEmpty()) {
         this.set.clear();
         this.fireChangeEvent();
      }
   }

   @Override
   public void remove(T... items) {
      if (this.set.removeAll(Arrays.asList(items))) {
         this.fireChangeEvent();
      }
   }

   @Override
   public boolean isEmpty() {
      return this.set.isEmpty();
   }
}
