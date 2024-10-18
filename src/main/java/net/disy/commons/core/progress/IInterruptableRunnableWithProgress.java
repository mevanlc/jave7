package net.disy.commons.core.progress;

import java.lang.reflect.InvocationTargetException;

public interface IInterruptableRunnableWithProgress {
   void run(IProgressMonitor var1, ICancelable var2) throws InterruptedException, InvocationTargetException;
}
