package net.dizzy.commons.core.util;

public final class Ensure {
   private Ensure() {
   }

   public static void ensureArgumentNotNull(Object value) {
      if (value == null) {
         throw new IllegalArgumentException("Argument must not be null");
      }
   }

   public static void ensureArgumentTrue(String message, boolean condition) {
      if (!condition) {
         throw new IllegalArgumentException(message);
      }
   }

   public static void ensureArrayIndex(int index, int min, int max) {
      if (index < min || index > max) {
         throw new IndexOutOfBoundsException(index + " not in [" + min + ", " + max + "]");
      }
   }

   public static void ensureTrue(String message, boolean condition) {
      if (!condition) {
         throw new IllegalStateException(message);
      }
   }

   public static void ensureNotNull(Object value) {
      if (value == null) {
         throw new NullPointerException();
      }
   }

   public static void ensureNotNull(String message, Object value) {
      if (value == null) {
         throw new NullPointerException(message);
      }
   }

   public static void ensureArgumentInstanceOf(Object value, Class<?> type) {
      ensureArgumentNotNull(value);
      ensureArgumentNotNull(type);
      if (!type.isInstance(value)) {
         throw new IllegalArgumentException("Argument must be a " + type.getName());
      }
   }

   public static void ensureArgumentArrayContentsNotNull(Object[] values) {
      ensureArgumentNotNull(values);
      for (Object value : values) {
         ensureArgumentNotNull(value);
      }
   }
}
