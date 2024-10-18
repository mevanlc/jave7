package net.disy.commons.core.util;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.disy.commons.core.predicate.IPredicate;
import net.disy.commons.core.string.StringConcatenationBuilder;

public class ArrayUtilities {
   public static <T> T[] filter(T[] array, IPredicate<T> predicate) {
      List<T> selected = new ArrayList<>();

      for (T element : array) {
         if (predicate.evaluate(element)) {
            selected.add(element);
         }
      }

      T[] newArray = (T[])Array.newInstance(array.getClass().getComponentType(), selected.size());
      return selected.toArray(newArray);
   }

   public static <T, E extends Exception> T getSingleRequired(T[] array, IPredicate<T> predicate, E e) throws E {
      T[] found = filter(array, predicate);
      if (found.length != 1) {
         e.initCause(new IllegalStateException(MessageFormat.format("{0} objects found.", found.length)));
         throw e;
      } else {
         return found[0];
      }
   }

   public static <T> void forAllDo(T[] array, IClosure<T> closure) {
      for (T element : array) {
         closure.execute(element);
      }
   }

   public static Class<?> getSharedSuperType(Class<?> componentType1, Class<?> componentType2) {
      if (componentType1.isAssignableFrom(componentType2)) {
         return componentType1;
      } else {
         return componentType2.isAssignableFrom(componentType1) ? componentType2 : getSharedSuperType(componentType1.getSuperclass(), componentType2);
      }
   }

   public static <T> T[] concat(T object, T[] array2, Class<T> clazz) {
      T[] array1 = (T[])Array.newInstance(clazz, 1);
      array1[0] = object;
      return concat(clazz, array1, array2);
   }

   public static <T> T[] concat(Class<T> clazz, T[] array1, T... array2) {
      if (array2 == null) {
         return array1;
      } else if (array1 == null) {
         return array2;
      } else {
         T[] mergedArray = (T[])Array.newInstance(clazz, array1.length + array2.length);
         System.arraycopy(array1, 0, mergedArray, 0, array1.length);
         System.arraycopy(array2, 0, mergedArray, array1.length, array2.length);
         return mergedArray;
      }
   }

   public static <T, U> Map<T, U> createMap(U[] objects, IKeyProvider<T, U> keyProvider) {
      Map<T, U> map = new HashMap<>();

      for (U object : objects) {
         map.put(keyProvider.getKey(object), object);
      }

      return map;
   }

   public static int min(int[] values) {
      int min = Integer.MAX_VALUE;

      for (int value : values) {
         if (value < min) {
            min = value;
         }
      }

      return min;
   }

   public static int[] toPrimitive(Integer[] integerArray) {
      int[] intArray = new int[integerArray.length];

      for (int index = 0; index < intArray.length; index++) {
         intArray[index] = integerArray[index];
      }

      return intArray;
   }

   public static long[] toPrimitive(Long[] longs) {
      long[] longArray = new long[longs.length];

      for (int index = 0; index < longArray.length; index++) {
         longArray[index] = longs[index];
      }

      return longArray;
   }

   public static double[] toPrimitive(Double[] doubles) {
      double[] doubleArray = new double[doubles.length];

      for (int index = 0; index < doubleArray.length; index++) {
         doubleArray[index] = doubles[index];
      }

      return doubleArray;
   }

   public static char[] toPrimitive(Character[] characterArray) {
      char[] charArray = new char[characterArray.length];

      for (int index = 0; index < charArray.length; index++) {
         charArray[index] = characterArray[index];
      }

      return charArray;
   }

   public static <T> boolean containsValue(T[] array, final T value) {
      return contains(array, new IPredicate<T>() {
         @Override
         public boolean evaluate(T actualValue) {
            return ObjectUtilities.equals(value, actualValue);
         }
      });
   }

   public static <T> boolean contains(T[] array, IPredicate<T> predicate) {
      for (T element : array) {
         if (predicate.evaluate(element)) {
            return true;
         }
      }

      return false;
   }

   public static <T> int[] getIndices(T[] values, T[] allValues) {
      int[] indices = new int[values.length];
      List<T> allValuesList = Arrays.asList(allValues);

      for (int i = 0; i < values.length; i++) {
         indices[i] = allValuesList.indexOf(values[i]);
      }

      return indices;
   }

   public static <T> T getFirst(T[] array, IPredicate<T> predicate) {
      T notFoundValue = null;
      return getFirst(array, predicate, notFoundValue);
   }

   public static <T> T getFirst(T[] array, IPredicate<T> predicate, T notFoundValue) {
      for (T element : array) {
         if (predicate.evaluate(element)) {
            return element;
         }
      }

      return notFoundValue;
   }

   public static <I, O> O[] transform(I[] array, Class<? super O> clazz, ITransformer<I, O> transformer) {
      O[] transformed = (O[])Array.newInstance(clazz, array.length);

      for (int i = 0; i < array.length; i++) {
         transformed[i] = transformer.transform(array[i]);
      }

      return transformed;
   }

   public static <I, O> O[] transform(I[] array, Class<O> clazz) {
      return transform(array, clazz, new CastingTransformer<>());
   }

   public static int indexOf(byte[] buffer, int value) {
      for (int i = 0; i < buffer.length; i++) {
         if (buffer[i] == value) {
            return i;
         }
      }

      return -1;
   }

   public static <T> int[] indicesOf(T[] values, IPredicate<T> predicate) {
      List<Integer> indices = new ArrayList<>();

      for (int i = 0; i < values.length; i++) {
         if (predicate.evaluate(values[i])) {
            indices.add(i);
         }
      }

      int[] intIndices = new int[indices.size()];

      for (int ix = 0; ix < indices.size(); ix++) {
         intIndices[ix] = indices.get(ix);
      }

      return intIndices;
   }

   public static String[] toStrings(int[] values) {
      String[] strings = new String[values.length];

      for (int i = 0; i < values.length; i++) {
         strings[i] = String.valueOf(values[i]);
      }

      return strings;
   }

   public static String[] toStrings(long[] values) {
      String[] strings = new String[values.length];

      for (int i = 0; i < values.length; i++) {
         strings[i] = String.valueOf(values[i]);
      }

      return strings;
   }

   public static <T> int indexOf(T[] values, IPredicate<T> predicate) {
      for (int i = 0; i < values.length; i++) {
         if (predicate.evaluate(values[i])) {
            return i;
         }
      }

      return -1;
   }

   public static <T> T[] subArray(T[] array, int startIndex) {
      Ensure.ensureArgumentIndexInBounds(startIndex, new Range(0, array.length));
      int newLength = array.length - startIndex;
      T[] subArray = (T[])Array.newInstance(array.getClass().getComponentType(), newLength);
      System.arraycopy(array, startIndex, subArray, 0, newLength);
      return subArray;
   }

   public static <T> T[] subArray(T[] array, int startIndex, int endIndex) {
      Ensure.ensureArgumentIndexInBounds(startIndex, new Range(0, array.length));
      Ensure.ensureArgumentIndexInBounds(endIndex, new Range(0, array.length));
      Ensure.ensureTrue("EndIndex must be at least startIndex.", endIndex >= startIndex);
      int newLength = endIndex - startIndex;
      T[] subArray = (T[])Array.newInstance(array.getClass().getComponentType(), newLength);
      System.arraycopy(array, startIndex, subArray, 0, newLength);
      return subArray;
   }

   public static <T> T getFirstIfAny(T[] values) {
      return values.length == 0 ? null : values[0];
   }

   public static <T> T[] reverse(T[] values) {
      T[] revertedValues = (T[])Array.newInstance(values.getClass().getComponentType(), values.length);

      for (int i = 0; i < revertedValues.length; i++) {
         revertedValues[i] = values[values.length - i - 1];
      }

      return revertedValues;
   }

   public static String getRepresentation(long[] array) {
      return getRepresentation(array, "[", ",", "]");
   }

   public static <T> String getRepresentation(T[] array) {
      return array == null ? null : CollectionUtilities.getRepresentation(Arrays.asList(array));
   }

   public static <T> String getRepresentation(T[] array, String separator) {
      return getRepresentation(array, "", separator, "");
   }

   public static String getRepresentation(long[] array, String prefix, String separator, String postfix) {
      if (array == null) {
         return null;
      } else {
         StringConcatenationBuilder builder = new StringConcatenationBuilder(separator);

         for (long value : array) {
            builder.append(String.valueOf(value));
         }

         return prefix + builder.getString() + postfix;
      }
   }

   public static <T> String getRepresentation(T[] array, String prefix, String separator, String postfix) {
      return array == null ? null : CollectionUtilities.getRepresentation(Arrays.asList(array), prefix, separator, postfix);
   }

   public static <T> String getRepresentation(T[] array, String prefix, String separator, String postfix, IFormatter<T> formatter) {
      return array == null ? null : CollectionUtilities.getRepresentation(Arrays.asList(array), prefix, separator, postfix, formatter);
   }

   public static <T, U extends T> U[] filterClass(T[] array, final Class<U> clazz) {
      T[] filteredArray = filter(array, new IPredicate<T>() {
         @Override
         public boolean evaluate(T object) {
            return clazz.isAssignableFrom(object.getClass());
         }
      });
      return transform(filteredArray, clazz, new ITransformer<T, U>() {
         @Override
         public U transform(T input) {
            return (U)input;
         }
      });
   }

   public static <T> T[] convert(T... items) {
      return items;
   }

   @Deprecated
   public static <T> T[] toArray(Iterable<T> items, Class<T> itemClass) {
      List<T> itemList = new ArrayList<>();

      for (T item : items) {
         itemList.add(item);
      }

      return itemList.toArray((T[]) Array.newInstance(itemClass, itemList.size()));
   }

   public static <T> T[] concat(Class<T> componentClass, T[]... arrays) {
      int size = 0;

      for (T[] array : arrays) {
         size += array.length;
      }

      T[] concat = (T[])Array.newInstance(componentClass, size);
      int pos = 0;

      for (T[] array : arrays) {
         System.arraycopy(array, 0, concat, pos, array.length);
         pos += array.length;
      }

      return concat;
   }

   public static boolean[] repeat(boolean value, int length) {
      boolean[] b = new boolean[length];
      Arrays.fill(b, value);
      return b;
   }

   public static <T> T[] repeat(Class<T> clazz, T value, int length) {
      T[] array = (T[])Array.newInstance(clazz, length);
      Arrays.fill(array, value);
      return array;
   }
}
