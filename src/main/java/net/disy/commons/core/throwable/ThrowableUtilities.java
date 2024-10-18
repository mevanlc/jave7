package net.disy.commons.core.throwable;

public class ThrowableUtilities {
   public static void throwAtRuntime(Throwable throwable) {
      if (throwable instanceof Error) {
         throw (Error)throwable;
      } else if (throwable instanceof RuntimeException) {
         throw (RuntimeException)throwable;
      } else {
         throw new RuntimeException(throwable);
      }
   }
}
