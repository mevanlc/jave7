package de.jdemo.framework.test;

import de.jdemo.framework.DemoCase;
import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.IDemoCaseRunnable;
import de.jdemo.framework.state.DemoState;

/**
 * @author Markus Gebhard
 */
public class DemoCaseTest extends AbstractDemoCaseTest {

  @Override
  protected IDemoCase createEmptyDemo() {
    return new DemoCase("demo") { //$NON-NLS-1$
      //empty
    };
  }

  @Override
  protected IDemoCase createCrashingDemo(final RuntimeException exception) {
    return new DemoCase("demo") { //$NON-NLS-1$
      public void demo() {
        throw exception;
      }
    };
  }

  @Override
  protected IDemoCase createValidSelfExitingDemo() {
    return new DemoCase("demo") { //$NON-NLS-1$
      public void demo() {
        show("hello world"); //$NON-NLS-1$
      }
    };
  }

  public void testExceptionInNewThreadInsideDemo() {
    //Exceptions in threads created inside demos can only be caught by using a ThredGroup
    //created by the given convenience method.
    final RuntimeException exception = new RuntimeException();
    final IDemoCase demoCase = new DemoCase("demo") {
      public void demo() throws InterruptedException {
        show("show call is needed for legal demo");
        new Thread(createThreadGroup(), new Runnable() {
          public void run() {
            throw exception;
          }
        }).start();
        Thread.sleep(1000); //demo duration longer than it takes to throw the exception
      }
    };
    final IDemoCaseRunnable runner = demoCase.createRunnable(false);
    runner.run();
    assertEquals(DemoState.CRASHED, runner.getState());
    assertEquals(exception, runner.getThrowable());
  }
}