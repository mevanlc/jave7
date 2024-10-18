package net.disy.commons.swing.dialog.progress;

import java.lang.reflect.InvocationTargetException;
import net.disy.commons.core.progress.DefaultRunnableExecuter;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.IInterruptableRunnableWithProgress;
import net.disy.commons.core.progress.INonInterruptableRunnableWithProgress;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.core.progress.IRunnableExecuter;
import net.disy.commons.core.util.Ensure;

public class ProgressMonitorExecutor {
   public static final int DEFAULT_MILLISECONDS_UNTIL_DIALOG_POPUP = 500;
   private final IProgressComponent progressComponent;
   private final InternalProgressDialogModel model;
   private int millisecondsUntilDialogPopup = 500;
   private static IRunnableExecuter defaultExecuter = new DefaultRunnableExecuter("runWithProgress");

   public static void setDeaultExecuter(IRunnableExecuter defaultExecuter) {
      Ensure.ensureArgumentNotNull(defaultExecuter);
      ProgressMonitorExecutor.defaultExecuter = defaultExecuter;
   }

   public ProgressMonitorExecutor(InternalProgressDialogModel model, IProgressComponent progressComponent) {
      this.model = model;
      this.progressComponent = progressComponent;
   }

   public void setMillisecondsUntilDialogPopup(int millisecondsUntilDialogPopup) {
      this.millisecondsUntilDialogPopup = millisecondsUntilDialogPopup;
   }

   public void run(final INonInterruptableRunnableWithProgress runnable, final IProgressMonitor monitor) throws InvocationTargetException {
      Runnable actualRunnable = new Runnable() {
         @Override
         public void run() {
            try {
               runnable.run(monitor);
            } catch (InvocationTargetException var22) {
               ProgressMonitorExecutor.this.model.crashed(var22);
            } catch (RuntimeException var23) {
               ProgressMonitorExecutor.this.model.crashed(var23);
            } catch (Error var24) {
               ProgressMonitorExecutor.this.model.crashed(var24);
            } catch (Throwable var25) {
               ProgressMonitorExecutor.this.model.crashed(new InvocationTargetException(var25));
            } finally {
               synchronized (ProgressMonitorExecutor.this.model) {
                  ProgressMonitorExecutor.this.model.finished();
                  ProgressMonitorExecutor.this.model.notifyAll();
               }

               ProgressMonitorExecutor.this.progressComponent.dispose();
            }
         }
      };
      defaultExecuter.execute(actualRunnable);

      try {
         synchronized (this.model) {
            if (!this.model.isFinished()) {
               this.model.wait(this.millisecondsUntilDialogPopup);
            }
         }

         if (!this.model.isFinished()) {
            this.progressComponent.show();
         }

         this.model.throwThrowableIfAny();
      } catch (InterruptedException var7) {
         throw new RuntimeException("InterruptedException during non interruptable progress", var7);
      }
   }

   public void run(final IInterruptableRunnableWithProgress runnable, final IProgressMonitor progressMonitor, final ICancelable cancelable) throws InterruptedException, InvocationTargetException {
      Runnable actualRunnable = new Runnable() {
         @Override
         public void run() {
            try {
               runnable.run(progressMonitor, cancelable);
            } catch (InterruptedException var25) {
               ProgressMonitorExecutor.this.model.interrupted(var25);
            } catch (InvocationTargetException var26) {
               ProgressMonitorExecutor.this.model.crashed(var26);
            } catch (RuntimeException var27) {
               ProgressMonitorExecutor.this.model.crashed(var27);
            } catch (Error var28) {
               ProgressMonitorExecutor.this.model.crashed(var28);
            } catch (Throwable var29) {
               ProgressMonitorExecutor.this.model.crashed(new InvocationTargetException(var29));
            } finally {
               synchronized (ProgressMonitorExecutor.this.model) {
                  ProgressMonitorExecutor.this.model.finished();
                  ProgressMonitorExecutor.this.model.notifyAll();
               }

               ProgressMonitorExecutor.this.progressComponent.dispose();
            }
         }
      };
      defaultExecuter.execute(actualRunnable);
      synchronized (this.model) {
         if (!this.model.isFinished()) {
            this.model.wait(this.millisecondsUntilDialogPopup);
         }
      }

      if (!this.model.isFinished()) {
         this.progressComponent.show();
      }

      this.model.throwThrowableIfAny();
   }
}
