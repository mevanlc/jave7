package de.jdemo.junit.test;

import junit.framework.TestCase;
import junit.framework.TestResult;

import de.jdemo.framework.DemoIdentifier;
import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.IDemoCaseRunnable;
import de.jdemo.framework.IDemoVisitor;
import de.jdemo.framework.PlainDemoCase;
import de.jdemo.framework.exceptions.IllegalDemoImplementationException;
import de.jdemo.framework.state.DemoState;
import de.jdemo.framework.state.IDemoStateChangeListener;
import de.jdemo.junit.DemoTestCase;
import de.jdemo.util.BooleanHolder;

/**
 * @author Markus Gebhard
 */
public class DemoTestCaseTest extends TestCase {
  public void testCreate() {
    final String name = "name";
    final DemoTestCase demoTestCase = new DemoTestCase(new IDemoCase() {
      @Override
      public IDemoCaseRunnable createRunnable(boolean allowExternalLaunches) {
        throw new UnsupportedOperationException();
      }

      public DemoIdentifier getIdentifier() {
        throw new UnsupportedOperationException();
      }

      public String getName() {
        return name;
      }

      public void setName(String name) {
        throw new UnsupportedOperationException();
      }

      public void accept(IDemoVisitor visitor) {
        throw new UnsupportedOperationException();
      }
    }, 1000);
    assertEquals(1, demoTestCase.countTestCases());
    assertEquals(name, demoTestCase.getName());
  }

  public void testRunsDemo() {
    final BooleanHolder runFlag = new BooleanHolder(false);
    final BooleanHolder cancelFlag = new BooleanHolder(false);
    final DemoTestCase demoTestCase = new DemoTestCase(new IDemoCase() {
      public void accept(IDemoVisitor visitor) {
        throw new UnsupportedOperationException();
      }

      @Override
      public IDemoCaseRunnable createRunnable(boolean allowExternalLaunches) {
        return new IDemoCaseRunnable() {
          public IDemoCase getDemo() {
            throw new UnsupportedOperationException();
          }

          public void demoCrashed(Throwable error) {
            throw new UnsupportedOperationException();
          }

          public void addDemoStateChangeListener(IDemoStateChangeListener listener) {
            throw new UnsupportedOperationException();
          }

          public DemoState getState() {
            return DemoState.FINISHED;
          }

          public Throwable getThrowable() {
            throw new UnsupportedOperationException();
          }

          public void exit() {
            throw new UnsupportedOperationException();
          }

          public void cancel() {
            throw new UnsupportedOperationException();
          }

          public void run() {
            runFlag.setValue(true);
          }

          public void removeDemoStateChangeListener(IDemoStateChangeListener listener) {
            throw new UnsupportedOperationException();
          }

          public void showCalled() throws IllegalDemoImplementationException {
            throw new UnsupportedOperationException();
          }
        };
      }

      public DemoIdentifier getIdentifier() {
        throw new UnsupportedOperationException();
      }

      public String getName() {
        return "foo";
      }

      public void setName(String name) {
        throw new UnsupportedOperationException();
      }
    }, 1000);

    demoTestCase.run();
    assertTrue(runFlag.getValue());
    assertFalse(cancelFlag.getValue());
  }

  public void testFailsWhenDemoFails() {
    final DemoTestCase demoTestCase = new DemoTestCase(new IDemoCase() {
      public void accept(IDemoVisitor visitor) {
        throw new UnsupportedOperationException();
      }

      @Override
      public IDemoCaseRunnable createRunnable(boolean allowExternalLaunches) {
        return new IDemoCaseRunnable() {
          public IDemoCase getDemo() {
            throw new UnsupportedOperationException();
          }

          public void demoCrashed(Throwable error) {
            throw new UnsupportedOperationException();
          }

          public void addDemoStateChangeListener(IDemoStateChangeListener listener) {
            throw new UnsupportedOperationException();
          }

          public void removeDemoStateChangeListener(IDemoStateChangeListener listener) {
            throw new UnsupportedOperationException();
          }

          public DemoState getState() {
            return DemoState.FINISHED;
          }

          public Throwable getThrowable() {
            throw new UnsupportedOperationException();
          }

          public void exit() {
            throw new UnsupportedOperationException();
          }

          public void cancel() {
            //nothing to do
          }

          public void run() {
            throw new IllegalArgumentException("expected exception in demo");
          }

          public void showCalled() throws IllegalDemoImplementationException {
            throw new UnsupportedOperationException();
          }
        };
      }

      public DemoIdentifier getIdentifier() {
        throw new UnsupportedOperationException();
      }

      public String getName() {
        return "foo";
      }

      public void setName(String name) {
        throw new UnsupportedOperationException();
      }
    }, 1000);

    try {
      demoTestCase.run();
      fail();
    }
    catch (final IllegalArgumentException expected) {
      //expected
    }
  }

  public void testFailsAtTimeout() {
    final BooleanHolder cancelFlag = new BooleanHolder(false);
    final DemoTestCase demoTestCase = new DemoTestCase(new IDemoCase() {
      public void accept(IDemoVisitor visitor) {
        throw new UnsupportedOperationException();
      }

      @Override
      public IDemoCaseRunnable createRunnable(boolean allowExternalLaunches) {
        return new IDemoCaseRunnable() {
          public IDemoCase getDemo() {
            throw new UnsupportedOperationException();
          }

          public void demoCrashed(Throwable error) {
            throw new UnsupportedOperationException();
          }

          public void addDemoStateChangeListener(IDemoStateChangeListener listener) {
            throw new UnsupportedOperationException();
          }

          public void removeDemoStateChangeListener(IDemoStateChangeListener listener) {
            throw new UnsupportedOperationException();
          }

          public DemoState getState() {
            return DemoState.RUNNING;
          }

          public Throwable getThrowable() {
            throw new UnsupportedOperationException();
          }

          public void exit() {
            throw new UnsupportedOperationException();
          }

          public void cancel() {
            cancelFlag.setValue(true);
          }

          public void run() {
            try {
              Thread.sleep(1000);
            }
            catch (InterruptedException e) {
              //nothing to do
            }
          }

          public void showCalled() throws IllegalDemoImplementationException {
            throw new UnsupportedOperationException();
          }
        };
      }

      public DemoIdentifier getIdentifier() {
        return new DemoIdentifier("className", "demoMethodName");
      }

      public String getName() {
        return "foo";
      }

      public void setName(String name) {
        throw new UnsupportedOperationException();
      }
    }, 0);

    final TestResult result = new TestResult();
    demoTestCase.run(result);
    assertEquals(1, result.errorCount());
    assertTrue(cancelFlag.getValue());
  }

  public void testRunsPlainDemoCaseWithoutError() {
    final PlainDemoCase demoCase = new PlainDemoCase() {
      @SuppressWarnings("unused")
      public void demoFoo() {
        //nothing to do
      }
    };
    demoCase.setName("demoFoo"); //$NON-NLS-1$
    final DemoTestCase demoTestCase = new DemoTestCase(demoCase, 1000);
    final TestResult result = demoTestCase.run();
    assertEquals(1, result.runCount());
    assertEquals(0, result.errorCount());
    assertEquals(0, result.failureCount());
  }
}