package net.disy.commons.core.exception;

public class PrintStackTraceExceptionHandler implements IExceptionHandler {
   @Override
   public void handle(Throwable exception) {
      exception.printStackTrace();
   }
}
