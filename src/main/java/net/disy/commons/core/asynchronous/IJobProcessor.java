package net.disy.commons.core.asynchronous;

import net.disy.commons.core.progress.ICancelable;

public interface IJobProcessor<T> {
   void process(ICancelable var1, T var2) throws InterruptedException;
}
