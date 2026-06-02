package net.dizzy.commons.core.util;

public final class ArrayUtilities {
   private ArrayUtilities() {
   }

   public static char[] toPrimitive(Character[] values) {
      char[] result = new char[values.length];
      for (int i = 0; i < values.length; i++) {
         result[i] = values[i].charValue();
      }
      return result;
   }
}
