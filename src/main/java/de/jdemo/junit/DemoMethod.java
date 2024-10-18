package de.jdemo.junit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.jdemo.annotation.Demo;
import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.IDemoCaseRunnable;
import de.jdemo.framework.state.DemoState;
import de.jdemo.framework.state.IDemoStateVisitor;

import org.junit.internal.runners.TestClass;
import org.junit.internal.runners.TestMethod;

public class DemoMethod extends TestMethod {

  private final Method method;

  public DemoMethod(final Method method, final TestClass testClass) {
    super(method, testClass);
    this.method = method;
  }

  @Override
  public long getTimeout() {
    final Demo annotation = method.getAnnotation(Demo.class);
    if (annotation == null) {
      return Demo.DEFAULT_TIMEOUT_MILLIS;
    }
    return annotation.timeout();
  }

  @Override
  public void invoke(final Object test)
      throws IllegalArgumentException,
      IllegalAccessException,
      InvocationTargetException {
    final IDemoCase demo = (IDemoCase) test;
    demo.setName(method.getName());
    final IDemoCaseRunnable runnable = demo.createRunnable(false);
    runnable.run();
    if (!runnable.getState().isTerminated()) {
      runnable.cancel();
    }
    final Throwable[] error = new Throwable[1];
    runnable.getState().accept(new IDemoStateVisitor() {
      public void visitFinished(final DemoState state) {
        //test succeeded
      }

      public void visitRunning(final DemoState state) {
        error[0] = new Error("Illegal state for a demo " //$NON-NLS-1$
            + demo.getIdentifier()
            + ": " //$NON-NLS-1$
            + state
            + " although being run/cancelled"); //$NON-NLS-1$
      }

      public void visitInitial(final DemoState state) {
        error[0] = new Error("Illegal state for a demo " //$NON-NLS-1$
            + demo.getIdentifier()
            + ": " //$NON-NLS-1$
            + state
            + " although being run/cancelled"); //$NON-NLS-1$
      }

      public void visitCrashed(final DemoState state) {
        error[0] = runnable.getThrowable();
      }

      public void visitStarting(final DemoState state) {
        error[0] = new Error("Illegal state for a demo " //$NON-NLS-1$
            + demo.getIdentifier()
            + ": " //$NON-NLS-1$
            + state
            + " although being run/cancelled"); //$NON-NLS-1$
      }
    });
    if (error[0] != null) {
      throw new InvocationTargetException(error[0]);
    }
  }
}