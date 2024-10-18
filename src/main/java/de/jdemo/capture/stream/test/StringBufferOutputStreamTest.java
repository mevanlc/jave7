package de.jdemo.capture.stream.test;

import java.io.PrintStream;

import junit.framework.TestCase;
import de.jdemo.capture.stream.StringBufferOutputStream;

/**
 * @author Markus Gebhard
 */
public class StringBufferOutputStreamTest extends TestCase {

  public void testWrites() {
    StringBuffer sb = new StringBuffer();
    System.setOut(new PrintStream(new StringBufferOutputStream(sb)));
    System.out.print("Hello World!"); //$NON-NLS-1$
    assertEquals("Hello World!", sb.toString());     //$NON-NLS-1$
  }

}
