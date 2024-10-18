package de.jave.jave;

public class Ascii {
   private Ascii() {
   }

   public static boolean isAscii(char ch) {
      return ch >= ' ' && ch <= '~';
   }
}
