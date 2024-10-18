package de.jave.text;

public class QuickStringIndexOutOfBoundsException extends StringIndexOutOfBoundsException {
   public QuickStringIndexOutOfBoundsException(int index, String message) {
      this("String index out of range: " + index + " " + message);
   }

   public QuickStringIndexOutOfBoundsException(int index) {
      this("String index out of range: " + index);
   }

   public QuickStringIndexOutOfBoundsException(String message) {
      super(message);
   }
}
