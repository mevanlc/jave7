package de.jdemo.framework.exceptions;

/**
 * @author Markus Gebhard
 */
public class DemoExecutionFailedError extends Error {
  
  public DemoExecutionFailedError(String message) {
    super(message);
  }
}