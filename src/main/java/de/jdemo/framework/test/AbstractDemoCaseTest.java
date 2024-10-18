package de.jdemo.framework.test;

import junit.framework.TestCase;

import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.IDemoCaseRunnable;
import de.jdemo.framework.state.DemoState;

/**
 * @author Markus Gebhard
 */
public abstract class AbstractDemoCaseTest extends TestCase {
  public final void testCreateRunnable() {
    final IDemoCase demoCase = createEmptyDemo();
    final IDemoCaseRunnable runner = demoCase.createRunnable(false);
    assertNotNull(runner);
    assertEquals(DemoState.INITIAL, runner.getState());
      assertNotSame("creating a runner must clone the original democase!", runner.getDemo(), demoCase); //$NON-NLS-1$
    assertNull(runner.getThrowable());
    assertEquals(demoCase.getIdentifier(), runner.getDemo().getIdentifier());
  }

  public final void testValidSelfExitingDemo() {
    final IDemoCase demoCase = createValidSelfExitingDemo();
    final IDemoCaseRunnable runner = demoCase.createRunnable(false);
    final MockDemoStateChangeListener mockListener = new MockDemoStateChangeListener();
    runner.addDemoStateChangeListener(mockListener);
    runner.run();
    assertEquals(DemoState.FINISHED, runner.getState());
    assertNull(runner.getThrowable());
    mockListener.verify(MockDemoStateChangeListener.VALID_DEMO_STATE_CHANGES);
  }

  public final void testCrashingDemo() {
    final RuntimeException exception = new RuntimeException();
    final IDemoCase demoCase = createCrashingDemo(exception);
    final IDemoCaseRunnable runner = demoCase.createRunnable(false);
    final MockDemoStateChangeListener mockListener = new MockDemoStateChangeListener();
    runner.addDemoStateChangeListener(mockListener);
    runner.run();
    assertEquals(DemoState.CRASHED, runner.getState());
    assertSame(exception, runner.getThrowable());
    mockListener.verify(MockDemoStateChangeListener.CRASHING_INSIDE_DEMO_METHOD_CHANGES);
  }

  protected abstract IDemoCase createEmptyDemo();

  protected abstract IDemoCase createValidSelfExitingDemo();

  protected abstract IDemoCase createCrashingDemo(RuntimeException exception);
}