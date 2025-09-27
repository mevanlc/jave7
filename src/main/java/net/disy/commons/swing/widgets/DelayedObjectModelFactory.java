package net.disy.commons.swing.navigate;

import net.disy.commons.core.asynchronous.AsynchronousDroppingJobProcessor;
import net.disy.commons.core.asynchronous.IJobProcessor;
import net.disy.commons.core.exception.IExceptionHandler;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.ProgressUtilities;

public class DelayedObjectModelFactory {
   public static <T> ObjectModel<T> createDelayedModel(final ObjectModel<T> model, final long delayMillis) {
      final ObjectModel<T> delayedModel = new ObjectModel<>();
      delayedModel.setValue(model.getValue());
      final AsynchronousDroppingJobProcessor<T> processor = new AsynchronousDroppingJobProcessor<>(new IJobProcessor<T>() {
         @Override
         public void process(ICancelable cancelable, T job) throws InterruptedException {
            ProgressUtilities.checkInterrupted(cancelable);
            Thread.sleep(delayMillis);
            ProgressUtilities.checkInterrupted(cancelable);
            delayedModel.setValue(job);
         }
      }, new IExceptionHandler() {
         @Override
         public void handle(Throwable exception) {
            if (exception instanceof Error) {
               throw (Error)exception;
            } else {
               throw new RuntimeException(exception);
            }
         }
      });
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            processor.startJob(model.getValue());
         }
      });
      return delayedModel;
   }
}
