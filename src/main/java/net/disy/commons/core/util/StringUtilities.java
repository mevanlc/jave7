package net.disy.commons.core.util;

public class StringUtilities {
   public static boolean isNullOrEmpty(String text) {
      return text == null || text.length() == 0;
   }

   public static boolean startsWithIgnoreCase(String string, String startString) {
      return string.length() < startString.length() ? false : string.substring(0, startString.length()).equalsIgnoreCase(startString);
   }

   public static boolean endsWithIgnoreCase(String string, String endString) {
      return string.length() < endString.length() ? false : string.substring(string.length() - endString.length()).equalsIgnoreCase(endString);
   }

   public static boolean isNullOrTrimmedEmpty(String text) {
      return text == null || text.trim().length() == 0;
   }

   public static final String toUpperCase(String string) {
      return string == null ? null : string.toUpperCase();
   }

   public static final String toLowerCase(String string) {
      return string == null ? null : string.toLowerCase();
   }

   public static String trim(String string) {
      return string == null ? null : string.trim();
   }

   public static String getDefaultIfEmpty(String value, String defaultValue) {
      return isNullOrTrimmedEmpty(value) ? defaultValue : value;
   }

   public static int length(String value) {
      return value == null ? -1 : value.length();
   }

   public static boolean containsValueIgnoreCase(String value, String... strings) {
      for (String string : strings) {
         if (value.equalsIgnoreCase(string)) {
            return true;
         }
      }

      return false;
   }

   public static String trimToMaxLength(String string, int maxLength) {
      return string != null && maxLength >= 0 ? string.substring(0, Math.min(string.length(), maxLength)) : null;
   }
}
