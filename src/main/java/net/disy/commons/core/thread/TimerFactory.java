package net.disy.commons.core.thread;

import java.util.Timer;
import java.util.TimerTask;
import net.disy.commons.core.exception.CentralExceptionHandling;
import net.disy.commons.core.exception.IExceptionHandler;
import net.disy.commons.core.provider.IProvider;
import net.disy.commons.core.util.Ensure;

public class TimerFactory {
   public static ITimer createTimer(String threadName) {
      IProvider<IExceptionHandler> exceptionHandlerProvider = new IProvider<IExceptionHandler>() {
         public IExceptionHandler getObject() {
            return CentralExceptionHandling.getInstance().getHandler();
         }
      };
      return createTimer(threadName, exceptionHandlerProvider);
   }

   public static ITimer createTimer(String threadName, final IProvider<IExceptionHandler> exceptionHandlerProvider) {
      Ensure.ensureArgumentNotNull(threadName);
      Ensure.ensureArgumentNotNull(exceptionHandlerProvider);
      final Timer timer = new Timer(threadName, true);
      return new ITimer() {
         @Override
         public void schedule(final ITimerTask timerTask, long delay) {
            Ensure.ensureArgumentNotNull(timerTask);
            TimerTask sunTask = new TimerTask() {
               @Override
               public void run() {
                  try {
                     timerTask.run();
                  } catch (Throwable var3) {
                     IExceptionHandler exceptionHandler = exceptionHandlerProvider.getObject();
                     exceptionHandler.handle(var3);
                  }
               }
            };
            timer.schedule(sunTask, delay);
         }

         @Override
         public void cancel() {
            timer.cancel();
         }
      };
   }
}
