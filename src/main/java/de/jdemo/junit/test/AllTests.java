package de.jdemo.junit.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Markus Gebhard
 */
public class AllTests {

  public static Test suite() {
    final TestSuite suite = new TestSuite("Test for de.jdemo.junit.test"); //$NON-NLS-1$
    //$JUnit-BEGIN$
    suite.addTestSuite(DemoTestCaseTest.class);
    //$JUnit-END$
    return suite;
  }
}
