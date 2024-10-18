package de.jdemo.junit;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import de.jdemo.framework.IDemo;
import de.jdemo.framework.IDemoCase;

public class JDemoSuiteClassRunner extends Runner {

  private static final class OldTestClassAdaptingListener implements TestListener {
    private final RunNotifier fNotifier;

    private OldTestClassAdaptingListener(final RunNotifier notifier) {
      fNotifier = notifier;
    }

    public void endTest(final Test test) {
      fNotifier.fireTestFinished(asDescription(test));
    }

    public void startTest(final Test test) {
      fNotifier.fireTestStarted(asDescription(test));
    }

    // Implement junit.framework.TestListener
    public void addError(final Test test, final Throwable t) {
      final Failure failure = new Failure(asDescription(test), t);
      fNotifier.fireTestFailure(failure);
    }

    private Description asDescription(final Test test) {
      return makeDescription(test);
    }

    public void addFailure(final Test test, final AssertionFailedError t) {
      addError(test, t);
    }
  }

  private final Test fTest;

  @Override
  public void run(final RunNotifier notifier) {
    final TestResult result = new TestResult();
    result.addListener(createAdaptingListener(notifier));
    fTest.run(result);
  }

  public static TestListener createAdaptingListener(final RunNotifier notifier) {
    return new OldTestClassAdaptingListener(notifier);
  }

  @Override
  public Description getDescription() {
    return makeDescription(fTest);
  }

  private static Description makeDescription(final Test test) {
    if (test instanceof DemoTestCase) {
      final DemoTestCase tc = (DemoTestCase) test;
      final Class<? extends IDemoCase> demoClass = tc.getClassUnderTest();
      return Description.createTestDescription(demoClass, tc.getName());
    }
    else if (test instanceof TestSuite) {
      final TestSuite ts = (TestSuite) test;
      final String name = ts.getName() == null ? "" : ts.getName(); //$NON-NLS-1$
      final Description description = Description.createSuiteDescription(name);
      final int n = ts.testCount();
      for (int i = 0; i < n; i++) {
        description.addChild(makeDescription(ts.testAt(i)));
      }
      return description;
    }
    else {
      // This is the best we can do in this case
      return Description.createSuiteDescription(test.getClass());
    }
  }

  public static final class ExceptionConvertingTest extends TestCase {
    private final Class<?> suiteClass;
    private final Exception e;

    public ExceptionConvertingTest() {
      suiteClass = null;
      e = null;
    }

    private ExceptionConvertingTest(final Class<?> suiteClass, final Exception e) {
      this.suiteClass = suiteClass;
      this.e = e;
    }

    public void testConversion() {
      fail("Demo conversion failed for class " //$NON-NLS-1$
          + suiteClass.getName()
          + ". Exception: " //$NON-NLS-1$
          + e.getClass().getName()
          + " " //$NON-NLS-1$
          + e.getMessage());
    }
  }

  public JDemoSuiteClassRunner(final Class<?> klass) {
    fTest = createTest(klass);
  }

  private static Test createTest(final Class<?> klass) {
    try {
      final IDemo demo = (IDemo) klass.getMethod("suite").invoke(null); //$NON-NLS-1$
      return Demo2TestConverter.createTest(demo);
    }
    catch (final Exception e) {
      return new ExceptionConvertingTest(klass, e);
    }
  }
}