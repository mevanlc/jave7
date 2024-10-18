package de.jdemo.framework.util;

import de.jdemo.framework.DemoCase;

/**
 * @author Markus Gebhard
 */
public final class ErrorDemoCase extends DemoCase {
  private final Throwable exception;

  public ErrorDemoCase(Throwable exception) {
    super("demoError");
    this.exception = exception;
  }

  public void demoError() throws Throwable {
    throw exception;
  }
 
  public Throwable getException() {
    return exception;
  }
}