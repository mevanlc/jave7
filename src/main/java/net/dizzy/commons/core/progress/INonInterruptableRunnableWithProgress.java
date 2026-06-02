package net.dizzy.commons.core.progress;

import java.lang.reflect.InvocationTargetException;

public interface INonInterruptableRunnableWithProgress {
   void run(IProgressMonitor monitor) throws InvocationTargetException;
}
