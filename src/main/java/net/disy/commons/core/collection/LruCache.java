package net.disy.commons.core.collection;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class LruCache<K, V> extends LinkedHashMap<K, V> {
   private final int maxSize;

   public LruCache(int maxSize) {
      super(maxSize * 4 / 3 + 1, 0.75F, true);
      this.maxSize = maxSize;
   }

   @Override
   public V put(K key, V value) {
      V previousValue = super.put(key, value);
      int size = this.size();
      if (size > this.maxSize) {
         throw new IllegalStateException(
            "Cache size (" + size + ") greater than limit (" + this.maxSize + "). Maybe a non-threadsafe cache was used multithreaded?"
         );
      } else {
         return previousValue;
      }
   }

   @Override
   protected boolean removeEldestEntry(Entry<K, V> eldest) {
      return this.size() > this.maxSize;
   }

   public Map<K, V> threadSafe() {
      return Collections.synchronizedMap(this);
   }
}
