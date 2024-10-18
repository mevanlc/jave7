package de.jdemo.capture;

/**
 * @author Markus Gebhard
 */
public class DemoCaptureException extends Exception {

  public DemoCaptureException(String message){
    super(message);
  }

  public DemoCaptureException(String message, Throwable cause){
    super(message, cause);
  }
}