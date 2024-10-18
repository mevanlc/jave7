package net.disy.commons.core.collection;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class SoftHashMap<K, V> {
   private final Map<K, SoftReference<V>> map = new HashMap<>();
   private final ReferenceQueue<V> queue = new ReferenceQueue<>();

   public V get(K key) {
      SoftReference<V> softReference = this.map.get(key);
      if (softReference == null) {
         return null;
      } else {
         V result = softReference.get();
         if (result == null) {
            this.map.remove(key);
         }

         return result;
      }
   }

   private void processQueue() {
      SoftHashMap.SoftValue<V> sv;
      while ((sv = (SoftHashMap.SoftValue<V>)this.queue.poll()) != null) {
         this.map.remove(sv.key);
      }
   }

   public void put(K key, V value) {
      this.processQueue();
      this.map.put(key, new SoftHashMap.SoftValue<>(value, this.queue, key));
   }

   public Object remove(K key) {
      this.processQueue();
      return this.map.remove(key);
   }

   public void clear() {
      this.map.clear();
   }

   public int size() {
      this.processQueue();
      return this.map.size();
   }

   private static class SoftValue<V> extends SoftReference<V> {
      private final Object key;

      private SoftValue(V k, ReferenceQueue<V> q, Object key) {
         super(k, q);
         this.key = key;
      }
   }
}
