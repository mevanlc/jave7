package de.jdemo.capture.gui.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Markus Gebhard
 */
public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for de.jdemo.capture.gui.test"); //$NON-NLS-1$
    //$JUnit-BEGIN$
    suite.addTestSuite(GuiDemoCaptureTest.class);
    //$JUnit-END$
    return suite;
  }
}