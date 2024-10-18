package net.disy.commons.core.exception;

public class UnreachableCodeReachedException extends RuntimeException {
   public UnreachableCodeReachedException() {
      this(null, null);
   }

   public UnreachableCodeReachedException(String s) {
      this(s, null);
   }

   public UnreachableCodeReachedException(Throwable nestedException) {
      this(null, nestedException);
   }

   public UnreachableCodeReachedException(String message, Throwable nestedException) {
      super(message, nestedException);
   }
}
