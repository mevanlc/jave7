package de.jdemo.framework.util.test;

import junit.framework.TestCase;

import de.jdemo.framework.IDemoCaseRunnable;
import de.jdemo.framework.state.DemoState;
import de.jdemo.framework.util.ErrorDemoCase;

/**
 * @author Markus Gebhard
 */
public class ErrorDemoCaseTest extends TestCase {

  public void test() {
    final RuntimeException cause = new RuntimeException("cause"); //$NON-NLS-1$
    final ErrorDemoCase demoCase = new ErrorDemoCase(cause);
    assertNotNull(demoCase.getIdentifier());
    final IDemoCaseRunnable runnable = demoCase.createRunnable(false);
    runnable.run();
    assertEquals(DemoState.CRASHED, runnable.getState());
    assertEquals(cause, runnable.getThrowable());
  }

}