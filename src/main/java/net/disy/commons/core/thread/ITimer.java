package net.disy.commons.core.thread;

public interface ITimer {
   void schedule(ITimerTask var1, long var2);

   void cancel();
}
