package net.dizzy.commons.core.progress;

public interface IProgressMonitor extends ICancelable {
   void beginTask(String name, int totalWork);

   void worked(int work);

   void subTask(String name);

   void done();

   void setCanceled(boolean canceled);

   void beginTaskWithUnknownTotalWork(String name);
}
