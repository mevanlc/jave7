package de.jdemo.framework;

/**
 * An actual demo case that can be launched using its runner.
 * @author Markus Gebhard
 */
public interface IDemoCase extends IDemo {

  /** Creates a runnable for running this demo case. 
   * @param allowExternalLaunches indicates whether this demo is allowed e.g. to launch a file in
   * an external software. This flag will be false if this demo is executed as test for example. */
  public IDemoCaseRunnable createRunnable(boolean allowExternalLaunches);

  public DemoIdentifier getIdentifier();
}