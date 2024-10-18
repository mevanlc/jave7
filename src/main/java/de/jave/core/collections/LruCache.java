package de.jave.core.collections;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LruCache<K, V> extends LinkedHashMap<K, V> {
   private final int capacity;

   public LruCache(int capacity) {
      super(capacity * 2 + 1, 0.75F, true);
      this.capacity = capacity;
   }

   @Override
   protected boolean removeEldestEntry(Entry<K, V> eldest) {
      return this.size() > this.capacity;
   }
}
