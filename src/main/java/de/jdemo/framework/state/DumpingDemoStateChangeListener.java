package de.jdemo.framework.state;

import java.io.PrintStream;


/**
 * @author Markus Gebhard
 */
public class DumpingDemoStateChangeListener implements IDemoStateChangeListener {
  private PrintStream printStream;

  public DumpingDemoStateChangeListener(PrintStream stream) {
    this.printStream = stream;
  }
  
  public void demoStateChanged(IDemoStateChangeEvent event) {
    printStream.println("Demo state changed: " + event.getOldState() + " -> " + event.getNewState()); //$NON-NLS-1$ //$NON-NLS-2$
  }
}