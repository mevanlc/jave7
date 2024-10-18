package net.disy.commons.core.collection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultipleValueHashMap<K, V> {
   private final Map<K, List<V>> map = new LinkedHashMap<>();

   public void clear() {
      this.map.clear();
   }

   public boolean containsKey(K key) {
      return this.map.containsKey(key);
   }

   @Override
   public boolean equals(Object obj) {
      return !(obj instanceof MultipleValueHashMap) ? false : this.map.equals(((MultipleValueHashMap)obj).map);
   }

   public Iterator<V> getIterator(K key) {
      List<V> list = this.map.get(key);
      return list != null ? list.iterator() : new NullIterator<>();
   }

   @Override
   public int hashCode() {
      return this.map.hashCode();
   }

   public boolean isEmpty() {
      return this.map.isEmpty();
   }

   public Set<K> keySet() {
      return this.map.keySet();
   }

   public void add(K key, V value) {
      List<V> list = this.map.get(key);
      if (list == null) {
         list = new ArrayList<>();
         this.map.put(key, list);
      }

      list.add(value);
   }

   public List<V> remove(K key) {
      return this.map.remove(key);
   }

   public int size() {
      return this.map.size();
   }

   @Override
   public String toString() {
      return this.map.toString();
   }

   public <T> T[] getArray(K key, Class<T> clazz) {
      List<V> list = this.getList(key);
      T[] array = (T[])Array.newInstance(clazz, list.size());
      return list.toArray(array);
   }

   public List<V> getList(K key) {
      List<V> list = this.map.get(key);
      return list != null ? list : new ArrayList<>(0);
   }

   public Iterable<V> getValues(K key) {
      return this.getList(key);
   }

   public Object[] getArray(K key) {
      return this.getArray(key, Object.class);
   }

   public LinkedHashSet<V> getSet(K key) {
      return new LinkedHashSet<>(this.getList(key));
   }

   public void addAll(K key, Collection<V> values) {
      for (V value : values) {
         this.add(key, value);
      }
   }

   public void addAll(MultipleValueHashMap<K, V> other) {
      for (K key : other.keySet()) {
         this.addAll(key, other.getList(key));
      }
   }
}
