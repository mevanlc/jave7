package net.disy.commons.core.progress;

public interface INonInterruptableResultReturningRunnableWithProgress<R, E extends Exception> {
   R run(IProgressMonitor var1) throws E;
}
