package de.jdemo.junit.test;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.List;

import javax.swing.JPanel;

import junit.framework.AssertionFailedError;

import de.jdemo.extensions.SwingDemoCase;
import de.jdemo.framework.IDemoCase;
import de.jdemo.junit.DemoAsTestRunner;

import org.junit.Test;
import org.junit.internal.runners.InitializationError;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class DemoAsTestRunnerTest {

  public static class DummyDemo extends SwingDemoCase {
    public void demo() {
      show(new JPanel());
    }

    public void demoFailing() throws Exception {
      throw new IllegalStateException();
    }
  }

  @Test
  public void demoIsConvertedToTest() throws Exception {
    final TestDemoAsJunit4Test demoAsTest = new TestDemoAsJunit4Test(DummyDemo.class);
    final List<Method> testMethods = demoAsTest.getTestMethods();
    assertEquals(2, testMethods.size());
    assertEquals("demo", testMethods.get(0).getName()); //$NON-NLS-1$
    assertEquals("demoFailing", testMethods.get(1).getName()); //$NON-NLS-1$
  }

  @Test
  public void demoIsExecutable() throws Exception {
    final TestDemoAsJunit4Test demoAsTest = new TestDemoAsJunit4Test(DummyDemo.class);
    final DummyDemo demo = (DummyDemo) demoAsTest.createTest();
    demo.demo();
  }

  @Test(expected = IllegalStateException.class)
  public void failingDemoFails() throws Exception {
    final TestDemoAsJunit4Test demoAsTest = new TestDemoAsJunit4Test(DummyDemo.class);
    final DummyDemo demo = (DummyDemo) demoAsTest.createTest();
    demo.demoFailing();
  }

  private class TestDemoAsJunit4Test extends DemoAsTestRunner {

    public TestDemoAsJunit4Test(final Class<? extends IDemoCase> klass) throws InitializationError {
      super(klass);
    }

    @Override
    public List<Method> getTestMethods() {
      return super.getTestMethods();
    }

    @Override
    public Object createTest() throws Exception {
      return super.createTest();
    }
  }

  @Test
  public void swingDemoCaseIsExecutedOnEventDispatchThread() throws InitializationError {
    final DemoAsTestRunner runner = new DemoAsTestRunner(MockTestSwingDemoCase.class);
    final RunNotifier notifier = new RunNotifier();
    notifier.addListener(new RunListener() {
      @Override
      public void testFailure(final Failure failure) throws Exception {
        throw new AssertionFailedError(failure.getMessage());
      }
    });
    runner.run(notifier);
  }
}