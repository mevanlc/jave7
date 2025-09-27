package net.disy.commons.swing.dialog.progress;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import net.disy.commons.core.progress.IInterruptableRunnableWithProgress;
import net.disy.commons.core.progress.INonInterruptableResultReturningRunnableWithProgress;
import net.disy.commons.core.progress.INonInterruptableRunnableWithProgress;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.core.progress.IRunnableContext;
import net.disy.commons.core.progress.IRunnableExecuter;
import net.disy.commons.core.util.Ensure;

public class ProgressMonitorDialog implements IRunnableContext {
   private boolean first = true;
   private final InternalProgressDialog progressDialog;
   private final InternalProgressDialogModel model = new InternalProgressDialogModel();
   private final ProgressMonitorExecutor progressMonitorExecutor;

   public static void setDefaultExecuter(IRunnableExecuter defaultExecuter) {
      ProgressMonitorExecutor.setDeaultExecuter(defaultExecuter);
   }

   public ProgressMonitorDialog(Component parentComponent, String title) {
      this.progressDialog = new InternalProgressDialog(parentComponent, title, this.model);
      this.progressMonitorExecutor = new ProgressMonitorExecutor(this.model, this.progressDialog);
   }

   public void setMillisecondsUntilDialogPopup(int millisecondsUntilDialogPopup) {
      this.progressMonitorExecutor.setMillisecondsUntilDialogPopup(millisecondsUntilDialogPopup);
   }

   @Override
   public void run(INonInterruptableRunnableWithProgress runnable) throws InvocationTargetException {
      Ensure.ensureArgumentNotNull(runnable);
      synchronized (this) {
         if (!this.first) {
            throw new IllegalStateException("Progress monitor dialog can only be run once.");
         }

         this.first = false;
      }

      this.progressDialog.setCancelable(false);
      this.progressMonitorExecutor.run(runnable, this.progressDialog);
   }

   @Override
   public void run(IInterruptableRunnableWithProgress runnable) throws InterruptedException, InvocationTargetException {
      Ensure.ensureArgumentNotNull(runnable);
      synchronized (this) {
         if (!this.first) {
            throw new IllegalStateException("Progress monitor dialog can only be run once.");
         }

         this.first = false;
      }

      this.progressDialog.setCancelable(true);
      this.progressMonitorExecutor.run(runnable, this.progressDialog, this.progressDialog);
   }

   public <R, E extends Exception> R run(final INonInterruptableResultReturningRunnableWithProgress<R, E> runnable) throws E, InvocationTargetException {
      class ResultReturningRunnable implements INonInterruptableRunnableWithProgress {
         private R result;

         @Override
         public void run(IProgressMonitor monitor) throws InvocationTargetException {
            try {
               this.result = runnable.run(monitor);
            } catch (RuntimeException var3) {
               throw var3;
            } catch (Exception var4) {
               throw new InvocationTargetException(var4);
            }
         }

         public R finish() {
            return this.result;
         }
      }

      ResultReturningRunnable resultReturningRunnable = new ResultReturningRunnable();

      try {
         this.run(resultReturningRunnable);
      } catch (InvocationTargetException var4) {
         throw (InvocationTargetException)var4.getTargetException();
      }

      return resultReturningRunnable.finish();
   }
}
