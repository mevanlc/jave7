package de.jdemo.framework.exceptions;

/**
 * @author Markus Gebhard
 */
public class DemoClassNotFoundException extends Exception {
  public DemoClassNotFoundException(String className) {
    super("Demo class '" + className + "' not found.");
  }
}