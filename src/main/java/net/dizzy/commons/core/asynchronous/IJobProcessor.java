package net.dizzy.commons.core.asynchronous;

import net.dizzy.commons.core.progress.ICancelable;

public interface IJobProcessor<T> {
   void process(ICancelable cancelable, T job) throws InterruptedException;
}
