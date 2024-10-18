package de.jdemo.framework.test;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Markus Gebhard
 */
public class AllTests {

  public static Test suite() {
    final TestSuite suite = new TestSuite("Test for de.jdemo.framework.test"); //$NON-NLS-1$
    //$JUnit-BEGIN$
    suite.addTestSuite(DemoIdentifierTest.class);
    suite.addTestSuite(DemoCaseTest.class);
    suite.addTest(new JUnit4TestAdapter(DemoSuiteTest.class));
    //$JUnit-END$
    return suite;
  }
}
