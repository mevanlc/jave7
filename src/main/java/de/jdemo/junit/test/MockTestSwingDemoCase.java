package de.jdemo.junit.test;

import javax.swing.SwingUtilities;

import de.jdemo.extensions.SwingDemoCase;

public class MockTestSwingDemoCase extends SwingDemoCase {

  public void demo() {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new IllegalStateException(
          "Expected to be executed on EDT, but was " + Thread.currentThread().getName()); //$NON-NLS-1$
    }
    show("foo"); //$NON-NLS-1$
  }
}