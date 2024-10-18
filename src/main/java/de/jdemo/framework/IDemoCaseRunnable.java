package de.jdemo.framework;

import de.jdemo.framework.exceptions.IllegalDemoImplementationException;
import de.jdemo.framework.state.DemoState;
import de.jdemo.framework.state.IDemoStateChangeListener;

/**
 * A <code>IDemoCaseRunner</code> is used to launch the demo and caries its state. 
 * @author Markus Gebhard
 */
public interface IDemoCaseRunnable extends Runnable {

  public IDemoCase getDemo();

  public void demoCrashed(Throwable error);

  public void addDemoStateChangeListener(IDemoStateChangeListener listener);
  
  public void removeDemoStateChangeListener(IDemoStateChangeListener listener);

  public DemoState getState();

  public Throwable getThrowable();

  public void exit();

  public void cancel();

  /** Notifies this runnable that a show method was called.
   * @throws IllegalDemoImplementationException if this method is called more often than once.
   */
  public void showCalled() throws IllegalDemoImplementationException;
}