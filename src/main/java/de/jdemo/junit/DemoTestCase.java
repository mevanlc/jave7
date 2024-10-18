package de.jdemo.junit;

import java.util.Timer;
import java.util.TimerTask;

import junit.framework.TestCase;
import junit.framework.TestResult;

import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.IDemoCaseRunnable;
import de.jdemo.framework.state.DemoState;
import de.jdemo.framework.state.IDemoStateVisitor;
import de.jdemo.util.BooleanHolder;

public class DemoTestCase extends TestCase {

  private final IDemoCase demo;
  private final long timeOutMillis;

  public DemoTestCase() {
    this.demo = null;
    this.timeOutMillis = -1;
  }

  public DemoTestCase(final IDemoCase demo, final long timeOutMillis) {
    super(demo.getName());
    this.demo = demo;
    this.timeOutMillis = timeOutMillis;
  }

  @Override
  public void run(final TestResult result) {
    runDemoCaseAsTest(demo, result);
  }

  public Class<? extends IDemoCase> getClassUnderTest() {
    return demo.getClass();
  }

  private void runDemoCaseAsTest(final IDemoCase demoCase, final TestResult result) {
    result.startTest(this);
    final IDemoCaseRunnable runner = demoCase.createRunnable(false);
    executeDemoCaseRunnable(demoCase, result, runner);
    result.endTest(this);
  }

  private void executeDemoCaseRunnable(
      final IDemoCase demoCase,
      final TestResult result,
      final IDemoCaseRunnable runner) {
    final BooleanHolder timeOutFlag = new BooleanHolder(false);
    final Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        timeOutFlag.setValue(true);
        runner.cancel();
      }
    }, timeOutMillis);

    runner.run();
    timer.cancel();
    if (!runner.getState().isTerminated()) {
      runner.cancel();
    }

    if (timeOutFlag.getValue()) {
      result.addError(DemoTestCase.this, new Error("Timeout when running demo "
          + demoCase.getIdentifier()
          + ": Demo method did not return within "
          + timeOutMillis
          + "ms."));
    }
    else {
      checkRunnableStateAfterTest(this, demoCase, result, runner);
    }
  }

  public static void checkRunnableStateAfterTest(
      final TestCase testCase,
      final IDemoCase demoCase,
      final TestResult result,
      final IDemoCaseRunnable runner) {
    runner.getState().accept(new IDemoStateVisitor() {
      public void visitFinished(final DemoState state) {
        //test succeeded
      }

      public void visitRunning(final DemoState state) {
        result.addError(testCase, new Error("Illegal state for a demo "
            + demoCase.getIdentifier()
            + ": "
            + state
            + " although being run/cancelled"));
      }

      public void visitInitial(final DemoState state) {
        result.addError(testCase, new Error("Illegal state for a demo "
            + demoCase.getIdentifier()
            + ": "
            + state
            + " although being run/cancelled"));
      }

      public void visitCrashed(final DemoState state) {
        result.addError(testCase, runner.getThrowable());
      }

      public void visitStarting(final DemoState state) {
        result.addError(testCase, new Error("Illegal state for a demo "
            + demoCase.getIdentifier()
            + ": "
            + state
            + " although being run/cancelled"));
      }
    });
  }
}