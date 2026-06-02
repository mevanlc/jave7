package net.dizzy.commons.core.progress;

import java.lang.reflect.InvocationTargetException;

public interface IInterruptableRunnableWithProgress {
   void run(IProgressMonitor monitor, ICancelable cancelable) throws InterruptedException, InvocationTargetException;
}
