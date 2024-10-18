package de.jdemo.capture.stream.test;

import junit.framework.TestSuite;

/**
 * @author Markus Gebhard
 */
public class AllTests {

  public static TestSuite suite() {
    TestSuite suite = new TestSuite("Test for de.jdemo.capture.stream.test"); //$NON-NLS-1$
    //$JUnit-BEGIN$
    suite.addTestSuite(SystemStreamTypeTest.class);
    suite.addTestSuite(StringBufferOutputStreamTest.class);
    suite.addTestSuite(DummyDemoCaseTest.class);
    //$JUnit-END$
    return suite;
  }
}