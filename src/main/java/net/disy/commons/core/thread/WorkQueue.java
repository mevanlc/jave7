package net.disy.commons.core.thread;

import java.util.LinkedList;
import net.disy.commons.core.logging.ILogger;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.ListenerList;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.IClosure;

@SuppressWarnings("removal")
public class WorkQueue implements IWorkQueue {
   private final ILogger logger;
   private static final String SHUTDOWN_MESSAGE = "Shutting down, will not accept any more work.";
   private final LinkedList<Runnable> queue;
   private final String queueName;
   private final int priority;
   private final boolean asDaemon;
   private final int threadCount;
   private ThreadGroup threadGroup;
   private boolean acceptingWork = true;
   private int activeWorkersCount;
   private final Condition workQueueFinished;
   private final BooleanModel busyModel = new BooleanModel();
   private final ListenerList<IWorkQueueListener> listeners = new ListenerList<>();

   public WorkQueue(ILogger logger, String queueName, int threadCount) {
      this(logger, queueName, threadCount, false);
   }

   public WorkQueue(ILogger logger, String queueName, int threadCount, boolean asDaemon) {
      this(logger, queueName, threadCount, asDaemon, 5, false);
   }

   public WorkQueue(ILogger logger, String queueName, int threadCount, boolean asDaemon, int priority, boolean startLater) {
      Ensure.ensureArgumentNotNull(logger);
      this.workQueueFinished = new Condition(logger, false);
      this.logger = logger;
      this.queueName = queueName;
      this.threadCount = threadCount;
      this.asDaemon = asDaemon;
      this.priority = priority;
      this.queue = new LinkedList<>();
      if (!startLater) {
         this.start();
      }
   }

   public void start() {
      Ensure.ensureNull("Working queue " + this.queueName + " already started.", this.threadGroup);
      this.threadGroup = new ThreadGroup(this.queueName);
      this.threadGroup.setDaemon(this.asDaemon);
      this.logger.info("WorkQueue launching : " + this.queueName + " \tThreadCount: " + this.threadCount);

      for (int i = 0; i < this.threadCount; i++) {
         WorkQueue.PoolWorker poolWorker = new WorkQueue.PoolWorker(this.threadGroup, this.queueName + "-T-" + i);
         poolWorker.setPriority(this.priority);
         poolWorker.start();
      }
   }

   @Override
   public void execute(Runnable r) throws IllegalStateException {
      if (this.acceptingWork) {
         synchronized (this.queue) {
            this.queue.addLast(r);
            int queueSize = this.queue.size();
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(this.queueName + ": enqueued Runnable[" + r.toString() + "]. Current size of queue: " + queueSize);
            }

            this.queue.notify();
         }

         this.fireWaitingJobsCountChangedEvent();
      } else {
         throw new IllegalStateException("Shutting down, will not accept any more work.");
      }
   }

   public final void stopAcceptingWork() {
      this.acceptingWork = false;
      this.logger.info("Shutting down, will not accept any more work.");
      synchronized (this.queue) {
         this.queue.notifyAll();
      }
   }

   public void waitForWorkQueueFinished(long timeout) throws InterruptedException {
      this.workQueueFinished.waitForTrue(timeout);
   }

   private synchronized void workStarted() {
      this.activeWorkersCount++;
      this.updateBusyModel();
      this.fireWaitingJobsCountChangedEvent();
   }

   private void fireWaitingJobsCountChangedEvent() {
      synchronized (this.queue) {
         this.listeners.forAllDo(new IClosure<IWorkQueueListener>() {
            public void execute(IWorkQueueListener listener) {
               listener.waitingJobsCountChanged(WorkQueue.this.queue.size());
            }
         });
      }
   }

   private synchronized void workStopped() {
      this.activeWorkersCount--;
      this.updateBusyModel();
      this.checkWorkQueueFinished();
   }

   private synchronized void checkWorkQueueFinished() {
      if (!this.acceptingWork && this.activeWorkersCount == 0 && this.queue.isEmpty()) {
         this.workQueueFinished.setTrue();
      }
   }

   private synchronized void updateBusyModel() {
      this.busyModel.setValue(this.activeWorkersCount > 0);
      this.listeners.forAllDo(new IClosure<IWorkQueueListener>() {
         public void execute(IWorkQueueListener listener) {
            listener.activeWorkersCountChanged(WorkQueue.this.activeWorkersCount);
         }
      });
   }

   public BooleanModel getBusyModel() {
      return this.busyModel;
   }

   public void addListener(IWorkQueueListener listener) {
      this.listeners.add(listener);
   }

   public void removeListener(IWorkQueueListener listener) {
      this.listeners.remove(listener);
   }

   private class PoolWorker extends Thread {
      private PoolWorker(ThreadGroup threadGroup, String threadName) {
         super(threadGroup, threadName);
         boolean asDeamon = threadGroup.isDaemon();
         this.setDaemon(asDeamon);
      }

      @Override
      public void run() {
         while (true) {
            Runnable r;
            synchronized (WorkQueue.this.queue) {
               while (true) {
                  if (WorkQueue.this.queue.isEmpty()) {
                     try {
                        if (WorkQueue.this.acceptingWork) {
                           WorkQueue.this.queue.wait();
                           continue;
                        }

                        WorkQueue.this.logger.debug(this.getName() + ": Ending PoolWorker");
                        WorkQueue.this.checkWorkQueueFinished();
                     } catch (InterruptedException var12) {
                        continue;
                     }

                     return;
                  }

                  r = WorkQueue.this.queue.removeFirst();
                  int queueSize = WorkQueue.this.queue.size();
                  if (WorkQueue.this.logger.isDebugEnabled()) {
                     WorkQueue.this.logger.debug(this.getName() + ": dequeued Runnable[" + r.toString() + "]. Current size of queue: " + queueSize);
                  }
                  break;
               }
            }

            try {
               WorkQueue.this.workStarted();
               r.run();
            } catch (Throwable var10) {
               WorkQueue.this.logger.error(var10);
            } finally {
               WorkQueue.this.workStopped();
            }
         }
      }
   }
}
