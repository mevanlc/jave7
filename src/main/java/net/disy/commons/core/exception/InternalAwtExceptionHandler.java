package net.disy.commons.core.exception;

public class InternalAwtExceptionHandler implements IExceptionHandler {
   @Override
   public void handle(Throwable exception) {
      CentralExceptionHandling.getInstance().handle(exception);
   }
}
