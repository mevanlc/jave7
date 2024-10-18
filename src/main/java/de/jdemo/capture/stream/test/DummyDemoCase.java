package de.jdemo.capture.stream.test;

import de.jdemo.framework.DemoCase;

/**
 * @author Markus Gebhard
 */
public class DummyDemoCase extends DemoCase {
  public static String SYSTEM_OUT_TEXT= "Hello (out) World"; //$NON-NLS-1$
  public static String SYSTEM_ERR_TEXT= "Hello (err) World"; //$NON-NLS-1$

  public void demoSystemOut(){
    System.out.print(SYSTEM_OUT_TEXT);
  }

  public void demoSystemErr(){
    System.err.print(SYSTEM_ERR_TEXT);
  }

  public void demoThrowsException(){
    throw new IllegalStateException("Demo exception."); //$NON-NLS-1$
  }
}