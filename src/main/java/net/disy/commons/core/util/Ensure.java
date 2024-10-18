package net.disy.commons.core.util;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;

public class Ensure {
   public static void ensureNotNull(String message, Object object) {
      ensureTrue(message, object != null);
   }

   public static void ensureArgumentNotNull(String message, Object object) throws IllegalArgumentException {
      ensureArgumentTrue(message, object != null);
   }

   public static void ensureNotNull(Object object) {
      ensureNotNull("Object must not be null", object);
   }

   public static void ensureArgumentNotNull(Object object) throws IllegalArgumentException {
      ensureArgumentNotNull("Object must not be null", object);
   }

   public static void ensureFalse(String message, boolean state) {
      ensureTrue(message, !state);
   }

   public static void ensureTrue(String message, boolean state) {
      if (!state) {
         throw new ContractFailedException(message);
      }
   }

   public static void ensureArgumentTrue(String message, boolean state) throws IllegalArgumentException {
      if (!state) {
         throw new IllegalArgumentException(message);
      }
   }

   public static void ensureArgumentEquals(String message, Object expected, Object argument) {
      ensureArgumentTrue(message, ObjectUtilities.equals(expected, argument));
   }

   public static void ensureArgumentEquals(String message, int expected, int argument) {
      ensureArgumentTrue(message, expected == argument);
   }

   public static void ensureArrayIndex(int index, int minIndex, int maxIndex) {
      if (index < minIndex) {
         throw new ArrayIndexOutOfBoundsException(index + " < " + minIndex);
      } else if (index > maxIndex) {
         throw new ArrayIndexOutOfBoundsException(index + " > " + maxIndex);
      }
   }

   public static void ensureArgumentFalse(String message, boolean argumentState) {
      ensureArgumentTrue(message, !argumentState);
   }

   public static void ensureArgumentArrayContentsNotNull(Object[] arguments) {
      if (arguments != null) {
         String message = "Array contents must not be null";

         for (int i = 0; i < arguments.length; i++) {
            ensureArgumentNotNull("Array contents must not be null at index " + i + " (0.." + (arguments.length - 1) + ')', arguments[i]);
         }
      }
   }

   public static void ensureValueWithin(double value, double minValue, double maxValue) {
      if (value < minValue) {
         throw new IllegalArgumentException(value + " < " + minValue);
      } else if (value > maxValue) {
         throw new ArrayIndexOutOfBoundsException(value + " > " + maxValue);
      }
   }

   public static void ensureEqual(Object obj1, Object obj2) {
      if (!ObjectUtilities.equals(obj1, obj2)) {
         throw new ContractFailedException("The Objects " + obj1 + " and " + obj2 + " are not equal.");
      }
   }

   public static void ensureArgumentEquals(int expected, int actual) {
      ensureArgumentEquals(MessageFormat.format("expected {0}, actual {1}", expected, actual), expected, actual);
   }

   public static void ensureArgumentInstanceOf(Object value, Class<?> expectedClass) {
      ensureArgumentNotNull(value);
      if (!expectedClass.isInstance(value)) {
         throw new IllegalArgumentException(MessageFormat.format("Illegal class {0} - expected an instance of {1}", value.getClass(), expectedClass));
      }
   }

   public static void ensureClassAssignableFrom(Class<?> providedClass, Class<?> expectedClass) {
      ensureArgumentNotNull(providedClass);
      if (!expectedClass.isAssignableFrom(providedClass)) {
         throw new IllegalArgumentException(
            "Illegal class: expected class '" + expectedClass.getName() + "' is not assignable from provided class '" + providedClass + '\''
         );
      }
   }

   public static void ensureNull(Object object) {
      ensureTrue("Object must be null, was " + object, object == null);
   }

   public static void ensureArgumentIndexInBounds(int index, Range range) {
      if (!range.contains(index)) {
         throw new IllegalArgumentException(index + " not within " + range);
      }
   }

   public static void ensureArgumentArrayContains(Object[] array, Object value) {
      ensureArgumentTrue("Array does not contain " + value, Arrays.asList(array).contains(value));
   }

   public static void ensureArgumentArrayContains(String message, Object[] array, Object value) {
      ensureArgumentTrue(message, Arrays.asList(array).contains(value));
   }

   public static void ensureArgumentNull(String message, Object value) {
      if (value != null) {
         throw new IllegalArgumentException(message);
      }
   }

   public static void ensureNull(String message, Object value) {
      if (value != null) {
         throw new ContractFailedException(message);
      }
   }

   public static void ensureEqual(long expected, long actual) {
      ensureTrue("expected " + expected + ", was " + actual, expected == actual);
   }

   public static void ensureArgumentNotNullOrTrimmedEmpty(String argumentValue) {
      ensureArgumentNotNullOrTrimmedEmpty("Argument", argumentValue);
   }

   public static void ensureArgumentNotNullOrTrimmedEmpty(String argumentName, String argumentValue) {
      if (argumentValue == null) {
         String message = argumentName + " must not be null";
         throw new IllegalArgumentException(message);
      } else if (argumentValue.trim().length() == 0) {
         String message = argumentName + " must not be trimmed empty";
         throw new IllegalArgumentException(message);
      }
   }

   public static void ensureArgumentNotEmpty(Map<?, ?> map) {
      if (map.isEmpty()) {
         throw new IllegalArgumentException("Map must not be empty.");
      }
   }

   public static <T> void ensureArgumentNotNullOrEmpty(T[] array) {
      ensureArgumentNotNull(array);
      if (array.length == 0) {
         throw new IllegalArgumentException("Array must not be empty.");
      }
   }
}
