package de.jdemo.junit;

import junit.framework.Test;
import junit.framework.TestSuite;
import de.jdemo.annotation.Demo;
import de.jdemo.framework.IDemo;
import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.IDemoSuite;

/**
 * Converts a demo (or demo suite) to a unit test. The test ensures that the demo can be run
 * and cancelled without throwing an exception.
 * 
 * @author Markus Gebhard
 */
public class Demo2TestConverter {


  /** Creates a JUnit test from the given demo (democase or demosuite) using default timeout. 
   * @see #createTest(IDemo, long) */
  public static Test createTest(IDemo demo) {
    return createTest(demo, Demo.DEFAULT_TIMEOUT_MILLIS);
  }

  /** Creates a JUnit test from the given demo (democase or demosuite) using the specified timeout.
   * The test will fail with a timeout error if the demo method does not return within the given number of
   * milliseconds.  
   * 
   * @see #createTest(IDemo) */
  public static Test createTest(IDemo demo, long timeOutMillis) {
    if (demo instanceof IDemoCase) {
      return new DemoTestCase((IDemoCase) demo, timeOutMillis);
    }
    else if (demo instanceof IDemoSuite) {
      TestSuite testSuite = new TestSuite(demo.getName());
      IDemoSuite suite = (IDemoSuite) demo;
      for (int i = 0; i < suite.getDemoCount(); ++i) {
        IDemo childDemo = suite.getDemoAt(i);
        testSuite.addTest(createTest(childDemo, timeOutMillis));
      }
      return testSuite;
    }
    else {
      throw new IllegalStateException(demo + " is neither a demo nor a suite");
    }
  }
}