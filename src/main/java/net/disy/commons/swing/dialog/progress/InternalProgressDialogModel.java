package net.disy.commons.swing.dialog.progress;

import java.lang.reflect.InvocationTargetException;

public class InternalProgressDialogModel {
   private boolean canceled = false;
   private boolean finished = false;
   private InterruptedException interruptedException;
   private InvocationTargetException invocationTargetException;
   private RuntimeException runtimeException;
   private Error error;

   public void setCanceled(boolean canceled) {
      this.canceled = canceled;
   }

   public boolean isCanceled() {
      return this.canceled;
   }

   public void finished() {
      this.finished = true;
   }

   public boolean isFinished() {
      return this.finished;
   }

   public void interrupted(InterruptedException withInterruptedException) {
      this.interruptedException = withInterruptedException;
   }

   public void crashed(InvocationTargetException withInvocationTargetException) {
      this.invocationTargetException = withInvocationTargetException;
   }

   public void crashed(RuntimeException withRuntimeException) {
      this.runtimeException = withRuntimeException;
   }

   public void crashed(Error withError) {
      this.error = withError;
   }

   public void throwThrowableIfAny() throws InterruptedException, InvocationTargetException {
      if (this.error != null) {
         throw this.error;
      } else if (this.runtimeException != null) {
         throw this.runtimeException;
      } else if (this.interruptedException != null) {
         throw this.interruptedException;
      } else if (this.invocationTargetException != null) {
         throw this.invocationTargetException;
      }
   }
}
