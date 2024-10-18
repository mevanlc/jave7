package net.disy.commons.core.thread;

public interface IWorkQueueListener {
   void activeWorkersCountChanged(int var1);

   void waitingJobsCountChanged(int var1);
}
