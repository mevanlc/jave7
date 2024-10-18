package net.disy.commons.core.exception;

import java.lang.Thread.UncaughtExceptionHandler;

public class CentralExceptionHandling {
   private static final CentralExceptionHandling instance = new CentralExceptionHandling();
   private IExceptionHandler handler;

   private CentralExceptionHandling() {
      this.attachForEventDispatchExceptionHandling();
      this.attachForThreadUncaughtExceptionHandling();
   }

   private void attachForThreadUncaughtExceptionHandling() {
      Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
         @Override
         public void uncaughtException(Thread t, Throwable e) {
            CentralExceptionHandling.this.handle(e);
         }
      });
   }

   public IExceptionHandler getHandler() {
      return this.handler;
   }

   public static void setHandler(IExceptionHandler handler) {
      getInstance().handler = handler;
   }

   public static CentralExceptionHandling getInstance() {
      return instance;
   }

   public void handle(Throwable exception) {
      if (this.handler != null) {
         this.handler.handle(exception);
      } else {
         System.err.println("Exception occurred during event dispatching:");
         exception.printStackTrace();
      }
   }

   private void attachForEventDispatchExceptionHandling() {
      System.setProperty("sun.awt.exception.handler", InternalAwtExceptionHandler.class.getName());
   }
}
