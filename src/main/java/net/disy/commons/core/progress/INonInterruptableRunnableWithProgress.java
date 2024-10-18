package net.disy.commons.core.progress;

import java.lang.reflect.InvocationTargetException;

public interface INonInterruptableRunnableWithProgress {
   void run(IProgressMonitor var1) throws InvocationTargetException;
}
