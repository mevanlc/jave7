package net.disy.commons.core.progress;

public interface IProgressMonitor {
   int UNKNOWN = -1;

   void beginTask(String var1, int var2);

   void beginTaskWithUnknownTotalWork(String var1);

   void done();

   void setCanceled(boolean var1);

   void subTask(String var1);

   void worked(int var1);
}
