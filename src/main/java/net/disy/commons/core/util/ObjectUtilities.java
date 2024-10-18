package net.disy.commons.core.util;

public class ObjectUtilities {
   public static final boolean equals(Object[] o1, Object[] o2) {
      if (o1 == null && o2 == null) {
         return true;
      } else if (o1 != null && o2 != null && o1.length == o2.length) {
         for (int i = 0; i < o1.length; i++) {
            if (!equals(o1[i], o2[i])) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static final boolean equals(Object o1, Object o2) {
      return o1 == null && o2 == null || o1 != null && o1.equals(o2);
   }

   public static final int getHashCode(Object object) {
      return object == null ? 1 : object.hashCode();
   }

   public static int getHashCode(boolean value) {
      return value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
   }

   public static int getHashCode(double value) {
      long bits = Double.doubleToLongBits(value);
      return getHashCode(bits);
   }

   public static int getHashCode(long value) {
      return (int)(value ^ value >>> 32);
   }

   public static boolean equals(double d1, double d2) {
      return d1 == d2 || Double.isNaN(d1) && Double.isNaN(d2);
   }

   public static boolean equals(float f1, float f2) {
      return f1 == f2 || Float.isNaN(f1) && Float.isNaN(f2);
   }

   public static String toString(Object object) {
      return object == null ? null : object.toString();
   }

   public static boolean toStringEquals(Object value1, Object value2) {
      return equals(toString(value1), toString(value2));
   }

   public static <T extends Comparable<? super T>> int compare(T c1, T c2) {
      if (c1 == null && c2 == null) {
         return 0;
      } else if (c1 == null) {
         return -1;
      } else {
         return c2 == null ? 1 : c1.compareTo(c2);
      }
   }
}
