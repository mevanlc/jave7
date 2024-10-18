package de.jdemo.extensions;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import de.jdemo.framework.AbstractDemoCase;
import de.jdemo.framework.IDemoCaseRunnable;

/**
 * @author Markus Gebhard
 */
public abstract class GuiDemoCase extends AbstractDemoCase {
  private boolean registeredDemoWindowOpened = false;
  private Window demoWindow;
  private DemoExitWindowListener exitWindowListener = new DemoExitWindowListener(this);

  public GuiDemoCase() {
    super();
  }

  public GuiDemoCase(final String name) {
    super(name);
  }

  @Override
  protected Object clone() {
    final GuiDemoCase clone = (GuiDemoCase) super.clone();
    clone.demoWindow = null;
    clone.exitWindowListener = new DemoExitWindowListener(clone);
    return clone;
  }

  @Override
  public IDemoCaseRunnable createRunnable(final boolean allowExternalLaunches) {
    return new GuiDemoCaseRunnable((GuiDemoCase) getClone());
  }

  protected void disposeDemoWindow() {
    if (demoWindow != null && demoWindow.isDisplayable()) {
      demoWindow.dispose();
    }
  }

  /** Registers the given window as demo window for this GuiDemoCase. Usually called by any framework method
   * that displays a window object. */
  protected final void registerDemoWindow(final Window window) {
    demoWindow = window;
    attachExitDemoWindowListener(window);
  }

  private boolean hasExitDemoWindowListenerAttached(final Window window) {
    final WindowListener[] listeners = window.getWindowListeners();
    for (int i = 0; i < listeners.length; ++i) {
      if (listeners[i] == exitWindowListener) {
        return true;
      }
    }
    return false;
  }

  private void attachExitDemoWindowListener(final Window window) {
    if (!hasExitDemoWindowListenerAttached(window)) {
      window.addWindowListener(exitWindowListener);
    }
  }

  private static class DemoExitWindowListener extends WindowAdapter {
    private final GuiDemoCase demoCase;

    public DemoExitWindowListener(final GuiDemoCase demoCase) {
      this.demoCase = demoCase;
    }

    @Override
    public void windowClosing(final WindowEvent e) {
      //received when user requests close() on the window
      demoCase.exit();
    }

    @Override
    public void windowOpened(final WindowEvent e) {
      demoCase.setRegisteredDemoWindowOpened(true);
    }

    @Override
    public void windowClosed(final WindowEvent e) {
      //received when window.dispose(); called
      demoCase.exit();
    }
  }

  @Override
  public void cancel() {
    super.cancel();
    disposeDemoWindow();
    exit();
  }

  @Override
  protected void exit() {
    if (demoWindow != null && hasExitDemoWindowListenerAttached(demoWindow)) {
      demoWindow.removeWindowListener(exitWindowListener);
    }
    super.exit();
  }

  /** Returns the demo window registered with this GuiDemoCase or <code>null</code>
   * if there is none registered. 
   * @see #registerDemoWindow(Window)
   */
  public Window getRegisteredDemoWindow() {
    return demoWindow;
  }

  @Override
  public void executeTearDown() throws Exception {
    super.executeTearDown();
    disposeDemoWindow();
  }

  public boolean isRegisteredDemoWindowOpened() {
    return registeredDemoWindowOpened;
  }

  protected void setRegisteredDemoWindowOpened(final boolean opened) {
    this.registeredDemoWindowOpened = opened;
  }
}