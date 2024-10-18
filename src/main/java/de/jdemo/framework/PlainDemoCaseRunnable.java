package de.jdemo.framework;

/**
 * @author Markus Gebhard
 */
public class PlainDemoCaseRunnable extends DemoCaseRunnable {

  public PlainDemoCaseRunnable(final PlainDemoCase demoCase) {
    super(demoCase);
  }

  @Override
  public void run() {
    final Thread thread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
      public void run() {
        runInternal();
      }
    });
    setRunThread(thread);
    thread.start();
  }

  @Override
  protected void checkShowWasCalled() {
    //nothing to do
  }
}