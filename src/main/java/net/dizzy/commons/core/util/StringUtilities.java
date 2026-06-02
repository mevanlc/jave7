package net.dizzy.commons.core.util;

public final class StringUtilities {
   private StringUtilities() {
   }

   public static boolean isNullOrTrimmedEmpty(String value) {
      return value == null || value.trim().isEmpty();
   }
}
