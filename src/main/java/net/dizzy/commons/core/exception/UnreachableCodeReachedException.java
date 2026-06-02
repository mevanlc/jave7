package net.dizzy.commons.core.exception;

public class UnreachableCodeReachedException extends RuntimeException {
   public UnreachableCodeReachedException() {
      super();
   }

   public UnreachableCodeReachedException(Throwable cause) {
      super(cause);
   }
}
