package de.jdemo.extensions;

import java.awt.Window;

import javax.swing.SwingUtilities;

import de.jdemo.framework.DemoCaseRunnable;

/**
 * @author Markus Gebhard
 */
public class GuiDemoCaseRunnable extends DemoCaseRunnable {

  public GuiDemoCaseRunnable(final GuiDemoCase demoCase) {
    super(demoCase);
  }

  @Override
  protected void runMainDemo() throws Throwable {
    final Throwable[] throwable = new Throwable[1];
    final GuiDemoCase demo = (GuiDemoCase) getDemo();
    final Runnable runnable = new Runnable() {
      @Override
      public void run() {
        try {
          demo.runDemo();
          final Window demoWindow = demo.getRegisteredDemoWindow();
          if (demoWindow != null && !demoWindow.isVisible()) {
            exit();
          }
          checkShowWasCalled();
        }
        catch (final Throwable e) {
          throwable[0] = e;
        }
      }
    };
    if (SwingUtilities.isEventDispatchThread()) {
      runnable.run();
    }
    else {
      SwingUtilities.invokeAndWait(runnable);
    }
    if (throwable[0] != null) {
      throw throwable[0];
    }
  }
}