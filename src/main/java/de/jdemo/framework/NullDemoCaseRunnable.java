package de.jdemo.framework;

import de.jdemo.framework.state.DemoState;

/**
 * @author Markus Gebhard
 */
public class NullDemoCaseRunnable extends DemoCaseRunnable {

  public NullDemoCaseRunnable(final PlainDemoCase demoCase) {
    super(demoCase);
  }

  @Override
  public void run() {
    setState(DemoState.FINISHED);
  }

  @Override
  protected void checkShowWasCalled() {
    //nothing to do
  }
}