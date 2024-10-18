package de.jdemo.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Markus Gebhard
 */
public class AllJDemoTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for de.jdemo.suite"); //$NON-NLS-1$
    
    suite.addTest(de.jdemo.capture.stream.test.AllTests.suite());
    suite.addTest(de.jdemo.extensions.test.AllTests.suite()); 
    suite.addTest(de.jdemo.framework.test.AllTests.suite());
    suite.addTest(de.jdemo.framework.util.test.AllTests.suite());
    suite.addTest(de.jdemo.junit.test.AllTests.suite());
    suite.addTest(de.jdemo.capture.gui.test.AllTests.suite());
    //$JUnit-BEGIN$
    //$JUnit-END$
    return suite;
  }
}