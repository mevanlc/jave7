package net.dizzy.commons.core.exception;

public final class CentralExceptionHandling {
   private static IExceptionHandler handler = new PrintStackTraceExceptionHandler();

   private CentralExceptionHandling() {
   }

   public static void setHandler(IExceptionHandler exceptionHandler) {
      handler = exceptionHandler == null ? new PrintStackTraceExceptionHandler() : exceptionHandler;
      Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> handler.handle(throwable));
   }

   public static IExceptionHandler getHandler() {
      return handler;
   }

   public static void handle(Throwable throwable) {
      handler.handle(throwable);
   }
}
