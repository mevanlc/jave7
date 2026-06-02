package net.dizzy.commons.core.exception;

public class PrintStackTraceExceptionHandler implements IExceptionHandler {
   @Override
   public void handle(Throwable throwable) {
      throwable.printStackTrace();
   }
}
