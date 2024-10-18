package net.disy.commons.core.progress;

public class NullProgressMonitor implements IProgressMonitor {
   public static final IProgressMonitor INSTANCE = new NullProgressMonitor();

   @Override
   public void beginTask(String name, int totalWork) {
   }

   @Override
   public void beginTaskWithUnknownTotalWork(String name) {
   }

   @Override
   public void done() {
   }

   @Override
   public void setCanceled(boolean canceled) {
   }

   @Override
   public void subTask(String name) {
   }

   @Override
   public void worked(int work) {
   }

   @Override
   public boolean equals(Object object) {
      return object instanceof NullProgressMonitor;
   }

   @Override
   public int hashCode() {
      return this.getClass().hashCode();
   }
}
