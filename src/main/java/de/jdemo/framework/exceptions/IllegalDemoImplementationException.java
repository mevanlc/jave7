package de.jdemo.framework.exceptions;

import de.jdemo.framework.DemoIdentifier;

/**
 * @author Markus Gebhard
 */
public class IllegalDemoImplementationException extends RuntimeException {

  public IllegalDemoImplementationException(DemoIdentifier identifier, String message) {
    super(message + " In Demo " + identifier + ".");
  }

}