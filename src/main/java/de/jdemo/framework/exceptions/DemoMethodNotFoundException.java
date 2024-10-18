package de.jdemo.framework.exceptions;

/**
 * @author Markus Gebhard
 */
public class DemoMethodNotFoundException extends Exception {

  public DemoMethodNotFoundException(Class demoClass, String methodName) {
    super("Demo method '" + methodName + "' not found in demo class '" + demoClass + "'");
  }

}