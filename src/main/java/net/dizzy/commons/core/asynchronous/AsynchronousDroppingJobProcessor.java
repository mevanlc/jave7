package net.dizzy.commons.core.asynchronous;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import net.dizzy.commons.core.exception.IExceptionHandler;
import net.dizzy.commons.core.progress.ICancelable;

public class AsynchronousDroppingJobProcessor<T> {
   private final IJobProcessor<T> processor;
   private final IExceptionHandler exceptionHandler;
   private final ExecutorService executor = Executors.newSingleThreadExecutor();
   private final Object lock = new Object();
   private Cancelable currentCancelable;
   private T pendingJob;
   private boolean workerScheduled;

   public AsynchronousDroppingJobProcessor(IJobProcessor<T> processor, IExceptionHandler exceptionHandler) {
      this.processor = processor;
      this.exceptionHandler = exceptionHandler;
   }

   public void startJob(T job) {
      synchronized (lock) {
         if (currentCancelable != null) {
            currentCancelable.cancel();
         }
         pendingJob = job;
         if (!workerScheduled) {
            workerScheduled = true;
            executor.submit(this::runJobs);
         }
      }
   }

   private void runJobs() {
      while (true) {
         T job;
         Cancelable cancelable = new Cancelable();
         synchronized (lock) {
            job = pendingJob;
            pendingJob = null;
            currentCancelable = cancelable;
            if (job == null) {
               currentCancelable = null;
               workerScheduled = false;
               return;
            }
         }
         try {
            processor.process(cancelable, job);
         } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
         } catch (Throwable throwable) {
            if (exceptionHandler != null) {
               exceptionHandler.handle(throwable);
            }
         } finally {
            synchronized (lock) {
               if (currentCancelable == cancelable) {
                  currentCancelable = null;
               }
            }
         }
      }
   }

   private static final class Cancelable implements ICancelable {
      private final AtomicBoolean canceled = new AtomicBoolean();

      void cancel() {
         canceled.set(true);
      }

      @Override
      public boolean isCanceled() {
         return canceled.get();
      }
   }
}
