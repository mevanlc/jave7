package de.jdemo.framework;


/**
 * @author Markus Gebhard
 */
public class DemoCaseThreadGroup extends ThreadGroup {

    private IDemoCaseRunnable runner;

    public DemoCaseThreadGroup(IDemoCaseRunnable runner) {
        super(runner.getDemo().getIdentifier().getIdentifierName());
        this.runner = runner;
    }

    @SuppressWarnings({"removal", "RedundantSuppression"})
    public void uncaughtException(Thread t, Throwable error) {
        if (error instanceof ThreadDeath) {
            System.err.println("Demo thread stopped (" + error + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        } else if (runner != null) {
            runner.demoCrashed(error);
        } else {
            super.uncaughtException(t, error);
        }
    }
}