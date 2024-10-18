package net.disy.commons.core.io;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileDisplayNameUtilities {
   private static final Pattern WINDOWS_FILE_NAME_PATTERN = Pattern.compile("(.*?:[/|\\\\])(.*)([/|\\\\].*)");
   private static final Pattern UNIX_FILE_NAME_PATTERN = Pattern.compile("(/.*?/)(.*)(/.*)");

   public static String createShortenedFileName(String fileName, int maxLength) {
      if (fileName.length() <= maxLength) {
         return fileName;
      } else {
         Matcher windowsMatcher = WINDOWS_FILE_NAME_PATTERN.matcher(fileName);
         if (windowsMatcher.matches()) {
            String prefix = windowsMatcher.group(1);
            String interior = windowsMatcher.group(2);
            String postfix = windowsMatcher.group(3);
            return concatenateShortenedName(prefix, interior, postfix, maxLength);
         } else {
            Matcher unixMatcher = UNIX_FILE_NAME_PATTERN.matcher(fileName);
            if (unixMatcher.matches()) {
               String prefix = unixMatcher.group(1);
               String interior = unixMatcher.group(2);
               String postfix = unixMatcher.group(3);
               return concatenateShortenedName(prefix, interior, postfix, maxLength);
            } else {
               return fileName;
            }
         }
      }
   }

   private static String concatenateShortenedName(String prefix, String interior, String postfix, int maxLength) {
      String ellipsis = "...";
      int fixLength = prefix.length() + "...".length() + postfix.length();
      String interiorAddition = "";
      if (fixLength < maxLength) {
         interiorAddition = interior.substring(0, maxLength - fixLength);
      }

      return prefix + interiorAddition + "..." + postfix;
   }
}
