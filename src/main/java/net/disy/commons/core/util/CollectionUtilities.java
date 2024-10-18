package net.disy.commons.core.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.disy.commons.core.predicate.IPredicate;
import net.disy.commons.core.provider.IProvider;
import net.disy.commons.core.string.StringConcatenationBuilder;

public class CollectionUtilities {
   public static <T> void insertIntoSortedList(List<T> list, T item, Comparator<T> comparator) {
      insertIntoSortedList(list, item, comparator, 0, list.size());
   }

   private static <T> void insertIntoSortedList(List<T> list, T item, Comparator<T> comparator, int startIndex, int endIndex) {
      if (startIndex == endIndex) {
         list.add(startIndex, item);
      } else {
         int middleIndex = (startIndex + endIndex) / 2;
         T middleItem = list.get(middleIndex);
         if (comparator.compare(item, middleItem) < 0) {
            insertIntoSortedList(list, item, comparator, startIndex, middleIndex);
         } else {
            insertIntoSortedList(list, item, comparator, middleIndex + 1, endIndex);
         }
      }
   }

   public static <T> void forAllDo(Collection<T> collection, IClosure<T> closure) {
      Ensure.ensureArgumentNotNull(collection);
      Ensure.ensureArgumentNotNull(closure);

      for (T element : collection) {
         closure.execute(element);
      }
   }

   public static <T> void addCasted(Collection<T> valuesSet, Object object) {
      valuesSet.add((T)object);
   }

   public static <B> B[] toArray(Collection<B> collection, Class<B> clazz) {
      return (B[])collection.toArray((Object[])Array.newInstance(clazz, collection.size()));
   }

   public static int[] toPrimitiveArrayInt(Collection<Integer> collection) {
      int[] result = new int[collection.size()];
      int index = 0;

      for (Integer integer : collection) {
         if (integer == null) {
            throw new IllegalArgumentException();
         }

         result[index++] = integer;
      }

      return result;
   }

   public static double[] toPrimitiveArrayDouble(Collection<Double> collection) {
      double[] result = new double[collection.size()];
      int index = 0;

      for (Double integer : collection) {
         if (integer == null) {
            throw new IllegalArgumentException();
         }

         result[index++] = integer;
      }

      return result;
   }

   public static <I, O> O[] toArray(Iterable<I> iterable, Class<O> clazz, ITransformer<I, O> transformer) {
      List<O> result = new ArrayList<>();

      for (I item : iterable) {
         result.add(transformer.transform(item));
      }

      return toArray(result, clazz);
   }

   public static <I> I[] toArray(Iterable<I> iterable, Class<I> clazz) {
      List<I> result = new ArrayList<>();

      for (I item : iterable) {
         result.add(item);
      }

      return toArray(result, clazz);
   }

   public static <I, O> List<O> transform(Iterable<I> input, ITransformer<? super I, ? extends O> transformer) {
      List<O> outputList = new ArrayList<>();

      for (I inputElement : input) {
         outputList.add(transformer.transform(inputElement));
      }

      return outputList;
   }

   public static <T> boolean contains(Iterable<? extends T> iterable, IPredicate<T> predicate) {
      for (T value : iterable) {
         if (predicate.evaluate(value)) {
            return true;
         }
      }

      return false;
   }

   public static <T> T getFirst(Iterable<? extends T> iterable, IPredicate<T> predicate) {
      return getFirst(iterable, predicate, new NullProvider<>());
   }

   public static <T> T getFirst(Iterable<? extends T> iterable, IPredicate<T> predicate, IProvider<T> fallback) {
      if (iterable != null && predicate != null) {
         for (T item : iterable) {
            if (predicate.evaluate(item)) {
               return item;
            }
         }
      }

      return fallback.getObject();
   }

   public static <T> List<T> filter(Iterable<? extends T> iterable, IPredicate<T> predicate) {
      List<T> values = new ArrayList<>();
      if (iterable != null && predicate != null) {
         for (T item : iterable) {
            if (predicate.evaluate(item)) {
               values.add(item);
            }
         }
      }

      return values;
   }

   public static <T> List<T> copy(Iterable<? extends T> iterable) {
      List<T> values = new ArrayList<>();
      if (iterable != null) {
         for (T item : iterable) {
            values.add(item);
         }
      }

      return values;
   }

   public static <T> List<T> concat(List<T>... lists) {
      ArrayList<T> concatList = new ArrayList<>();

      for (List<T> list : lists) {
         concatList.addAll(list);
      }

      return concatList;
   }

   public static <T extends Comparable<? super T>> T min(Iterable<? extends T> collection) {
      List<T> filteredList = filter(collection, new NonNullPredicate<>());
      return filteredList.isEmpty() ? null : Collections.min(filteredList);
   }

   public static <T extends Comparable<? super T>> T max(Iterable<? extends T> collection) {
      List<T> filteredList = filter(collection, new NonNullPredicate<>());
      return filteredList.isEmpty() ? null : Collections.max(filteredList);
   }

   public static String getRepresentation(Iterable<?> iterable) {
      return getRepresentation(iterable, "[", ",", "]");
   }

   public static <T> String getRepresentation(Iterable<T> iterable, String prefix, String separator, String postfix) {
      return getRepresentation(iterable, prefix, separator, postfix, new IFormatter<T>() {
         @Override
         public String format(T value) {
            return value instanceof Object[] ? ArrayUtilities.getRepresentation((T[])value) : String.valueOf(value);
         }
      });
   }

   public static <T> String getRepresentation(Iterable<T> iterable, String prefix, String separator, String postfix, IFormatter<T> formatter) {
      if (iterable == null) {
         return null;
      } else {
         StringConcatenationBuilder builder = new StringConcatenationBuilder(separator);

         for (T value : iterable) {
            builder.append(formatter.format(value));
         }

         return prefix + builder.getString() + postfix;
      }
   }

   public static final <T> boolean equals(Collection<T> i1, Collection<T> i2) {
      return ObjectUtilities.equals(i1 == null ? null : i1.toArray(), i2 == null ? null : i2.toArray());
   }

   public static <T> int indexOf(List<T> values, IPredicate<T> predicate) {
      for (int i = 0; i < values.size(); i++) {
         if (predicate.evaluate(values.get(i))) {
            return i;
         }
      }

      return -1;
   }

   public static <T> int count(List<T> values, IPredicate<T> predicate) {
      int count = 0;

      for (T value : values) {
         if (predicate.evaluate(value)) {
            count++;
         }
      }

      return count;
   }

   public static <T> int[] indicesOf(List<T> values, IPredicate<T> predicate) {
      List<Integer> indices = new ArrayList<>();

      for (int i = 0; i < values.size(); i++) {
         if (predicate.evaluate(values.get(i))) {
            indices.add(i);
         }
      }

      return ArrayUtilities.toPrimitive(indices.toArray(new Integer[0]));
   }

   public static <T> boolean hasAnySameEntry(Collection<T> collection, Collection<T> other) {
      if (other.isEmpty()) {
         return false;
      } else {
         for (final T entry : collection) {
            if (contains(other, new IPredicate<T>() {
               @Override
               public boolean evaluate(T value) {
                  return entry == value;
               }
            })) {
               return true;
            }
         }

         return false;
      }
   }

   public static <T> boolean containsDuplicates(Collection<T> values) {
      return values.size() > new HashSet<>(values).size();
   }

   public static <T> int indexOf(Iterable<T> values, IPredicate<T> predicate) {
      int counter = 0;

      for (T value : values) {
         if (predicate.evaluate(value)) {
            return counter;
         }

         counter++;
      }

      return -1;
   }

   public static <T> Set<T> createSet(T... elements) {
      HashSet<T> set = new HashSet<>();
      Collections.addAll(set, elements);
      return set;
   }

   public static <T> Collection<T> subCollection(List<T> list, int startIndex) {
      int counter = 0;
      List<T> sublist = new ArrayList<>();

      for (T value : list) {
         if (counter >= startIndex) {
            sublist.add(value);
         }

         counter++;
      }

      return sublist;
   }
}
