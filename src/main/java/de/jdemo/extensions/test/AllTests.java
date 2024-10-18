package de.jdemo.extensions.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Markus Gebhard
 */
public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for de.jdemo.extensions.test"); //$NON-NLS-1$
    //$JUnit-BEGIN$
    suite.addTestSuite(SwingDemoCaseTest.class);
    suite.addTestSuite(AwtDemoCaseTest.class);
    //$JUnit-END$
    return suite;
  }
}
