package net.disy.commons.core.asynchronous;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.disy.commons.core.exception.IExceptionHandler;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.progress.DefaultCancelable;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.ProgressUtilities;
import net.disy.commons.core.util.Ensure;

public class AsynchronousDroppingJobProcessor<T> {
   private final IJobProcessor<T> processor;
   private final Executor executor = new ThreadPoolExecutor(0, 1, 2L, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
   private final BooleanModel busyModel = new BooleanModel(false);
   private DefaultCancelable currentJobCancelable;
   private final IExceptionHandler exceptionHandler;

   public AsynchronousDroppingJobProcessor(IJobProcessor<T> processor, IExceptionHandler exceptionHandler) {
      Ensure.ensureArgumentNotNull(processor);
      Ensure.ensureArgumentNotNull(exceptionHandler);
      this.processor = processor;
      this.exceptionHandler = exceptionHandler;
   }

   public void startJob(final T job) {
      Runnable runnable;
      synchronized (this) {
         if (this.currentJobCancelable != null) {
            this.currentJobCancelable.setCanceled(true);
         }

         this.currentJobCancelable = new DefaultCancelable();
         final ICancelable cancelable = this.currentJobCancelable;
         runnable = new Runnable() {
            @Override
            public void run() {
               AsynchronousDroppingJobProcessor.this.busyModel.setValue(true);

               try {
                  ProgressUtilities.checkInterrupted(cancelable);
                  AsynchronousDroppingJobProcessor.this.processor.process(cancelable, job);
                  AsynchronousDroppingJobProcessor.this.busyModel.setValue(false);
               } catch (InterruptedException var2) {
                  AsynchronousDroppingJobProcessor.this.busyModel.setValue(false);
               } catch (Throwable var3) {
                  AsynchronousDroppingJobProcessor.this.busyModel.setValue(false);
                  AsynchronousDroppingJobProcessor.this.exceptionHandler.handle(var3);
               }
            }
         };
      }

      this.executor.execute(runnable);
   }

   public BooleanModel getBusyModel() {
      return this.busyModel;
   }

   public void cancelCurrentJob() {
      synchronized (this) {
         if (this.currentJobCancelable != null) {
            this.currentJobCancelable.setCanceled(true);
         }

         this.currentJobCancelable = null;
      }
   }
}
