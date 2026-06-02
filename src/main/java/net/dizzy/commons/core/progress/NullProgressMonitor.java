package net.dizzy.commons.core.progress;

public class NullProgressMonitor implements IProgressMonitor {
   private boolean canceled;

   @Override
   public void beginTask(String name, int totalWork) {
   }

   @Override
   public void worked(int work) {
   }

   @Override
   public void subTask(String name) {
   }

   @Override
   public void done() {
   }

   @Override
   public boolean isCanceled() {
      return canceled;
   }

   @Override
   public void setCanceled(boolean canceled) {
      this.canceled = canceled;
   }

   @Override
   public void beginTaskWithUnknownTotalWork(String name) {
      beginTask(name, -1);
   }
}
