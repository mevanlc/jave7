package net.dizzy.commons.core.io;

public final class FileDisplayNameUtilities {
   private FileDisplayNameUtilities() {
   }

   public static String createShortenedFileName(String path, int maxLength) {
      if (path == null || path.length() <= maxLength || maxLength <= 3) {
         return path;
      }
      int tailLength = Math.max(1, maxLength - 3);
      return "..." + path.substring(path.length() - tailLength);
   }
}
