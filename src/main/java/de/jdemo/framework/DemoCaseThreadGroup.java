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

  public void uncaughtException(Thread t, Throwable error) {
    if (error instanceof ThreadDeath) {
      System.err.println("Demo thread stopped ("+error+")"); //$NON-NLS-1$ //$NON-NLS-2$
    }else
    if (!(error instanceof ThreadDeath)) {
      runner.demoCrashed(error);
    } else {
      super.uncaughtException(t, error);
    }
  }
}