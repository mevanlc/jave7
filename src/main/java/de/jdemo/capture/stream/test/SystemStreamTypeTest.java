package de.jdemo.capture.stream.test;

import java.io.PrintStream;

import junit.framework.TestCase;
import de.jdemo.capture.stream.StringBufferOutputStream;
import de.jdemo.capture.stream.SystemStreamType;

/**
 * @author Markus Gebhard
 */
public class SystemStreamTypeTest extends TestCase {

  public void testResetWithoutSet() {
    try {
      SystemStreamType.OUT.resetPrintStream();
      fail();
    }
    catch (IllegalStateException expected) {
      //expected
    }
  }

  public void testSetTwice() {
    try {
      SystemStreamType.OUT.setPrintStream(new PrintStream(new StringBufferOutputStream()));
      SystemStreamType.OUT.setPrintStream(new PrintStream(new StringBufferOutputStream()));
      fail();
    }
    catch (IllegalStateException expected) {
      //expected
    }
    SystemStreamType.OUT.resetPrintStream();
  }

  public void testResetTwice() {
    try {
      SystemStreamType.OUT.setPrintStream(new PrintStream(new StringBufferOutputStream()));
      SystemStreamType.OUT.resetPrintStream();
      SystemStreamType.OUT.resetPrintStream();
      fail();
    }
    catch (IllegalStateException expected) {
      //expected
    }
  }

  public void testOut() {
    StringBuffer sb = new StringBuffer();
    SystemStreamType.OUT.setPrintStream(new PrintStream(new StringBufferOutputStream(sb)));
    System.out.print("test"); //$NON-NLS-1$
    SystemStreamType.OUT.resetPrintStream();
    assertEquals("test", sb.toString()); //$NON-NLS-1$
  }

  public void testErr() {
    StringBuffer sb = new StringBuffer();
    SystemStreamType.ERR.setPrintStream(new PrintStream(new StringBufferOutputStream(sb)));
    System.err.print("test"); //$NON-NLS-1$
    SystemStreamType.ERR.resetPrintStream();
    assertEquals("test", sb.toString()); //$NON-NLS-1$
  }
}