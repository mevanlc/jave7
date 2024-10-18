package de.jave.figlet.util;

public class FigFileFormatException extends FigException {
   public FigFileFormatException(String message, Throwable nestedException) {
      super(message, nestedException);
   }

   public FigFileFormatException(String message) {
      super(message);
   }
}
