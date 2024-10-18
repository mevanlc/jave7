package de.jdemo.framework.util.test;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Markus Gebhard
 */
public class AllTests {

  public static Test suite() {
    final TestSuite suite = new TestSuite("Test for de.jdemo.framework.util.test"); //$NON-NLS-1$
    //$JUnit-BEGIN$
    suite.addTestSuite(DemoUtilitiesTest.class);
    suite.addTest(new JUnit4TestAdapter(DemoUtilities_GetDemoMethods_Test.class));
    suite.addTestSuite(ErrorDemoCaseTest.class);
    //$JUnit-END$
    return suite;
  }
}