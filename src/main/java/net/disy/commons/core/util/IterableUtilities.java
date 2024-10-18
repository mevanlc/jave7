package net.disy.commons.core.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import net.disy.commons.core.predicate.IPredicate;

public class IterableUtilities {
   public static <T> boolean containsValue(Iterable<T> values, T value) {
      for (T object : values) {
         if (ObjectUtilities.equals(value, object)) {
            return true;
         }
      }

      return false;
   }

   public static <T> T[] toArray(Iterable<T> values, Class<T> clazz) {
      Collection<T> list = asCollection(values);
      return list.toArray((T[]) Array.newInstance(clazz, list.size()));
   }

   public static <T> Collection<T> asCollection(Iterable<T> values) {
      Collection<T> collection = new ArrayList<>();

      for (T value : values) {
         collection.add(value);
      }

      return collection;
   }

   public static <T> Iterable<T> asIterable(T... values) {
      return Arrays.asList(values);
   }

   public static <T> Iterable<T> concat(Iterable<T>... iterables) {
      Collection<T> collection = new ArrayList<>();

      for (Iterable<T> iterable : iterables) {
         collection.addAll(asCollection(iterable));
      }

      return collection;
   }

   public static <T> T getFirst(Iterable<T> values, IPredicate<T> predicate) {
      for (T value : values) {
         if (predicate.evaluate(value)) {
            return value;
         }
      }

      return null;
   }
}
