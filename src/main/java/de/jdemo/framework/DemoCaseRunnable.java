package de.jdemo.framework;

import java.util.ArrayList;
import java.util.List;

import de.jdemo.framework.exceptions.IllegalDemoImplementationException;
import de.jdemo.framework.state.DemoState;
import de.jdemo.framework.state.IDemoStateChangeEvent;
import de.jdemo.framework.state.IDemoStateChangeListener;

/**
 * @author Markus Gebhard
 */
public class DemoCaseRunnable implements IDemoCaseRunnable {
  private Thread runThread;
  private Throwable error;
  private List listeners = new ArrayList();
  private DemoState state;
  private AbstractDemoCase demoCase;
  private boolean isShowCalled = false;

  public DemoCaseRunnable(AbstractDemoCase demoCase) {
    this.demoCase = demoCase;
    demoCase.setRunnable(this);
    setState(DemoState.INITIAL);
  }

  public final void cancel() {
    //Give the demo a chance to exit
    demoCase.cancel();
    if (runThread != null && runThread.isAlive()) {
      try {
        runThread.stop(); //deprecated but necessary in case cancel() has not stopped the thread
      }
      catch (Throwable t) {
        //nothing to do
      }
    }
  }

  public void run() {
    runInternal();
  }

  public void setRunThread(Thread runThread) {
    this.runThread = runThread;
  }
  
  protected void runInternal() {
    setState(DemoState.STARTING);
    try {
      demoCase.executeSetUp();
    }
    catch (Throwable e) {
      demoCrashed(e);
      try {
        demoCase.tearDown();
      }
      catch (Throwable e1) {
        //nothing to do
      }
      return;
    }
    try {
      setState(DemoState.RUNNING);
      runMainDemo();
    }
    catch (Throwable e) {
      if (e instanceof ThreadDeath) {
        setState(DemoState.FINISHED);
      }
      else {
        demoCrashed(e);
        try {
          demoCase.tearDown();
        }
        catch (Throwable e1) {
          //nothing to do
        }
      }
    }
  }

  protected void runMainDemo() throws Throwable {
    try {
      demoCase.runDemo();
      checkShowWasCalled();
    }
    catch (Throwable e) {
      if (!(e instanceof ThreadDeath)) {
        demoCrashed(e);
      }
    }
    finally {
      demoCase.exit();
    }
  }

  public DemoState getState() {
    return state;
  }

  protected void setState(DemoState state) {
    if (state == null) {
      throw new IllegalArgumentException("State is null"); //$NON-NLS-1$
    }
    if (state.equals(this.state)) {
      return;
    }

    if (DemoState.CRASHED.equals(this.state)) {
      return; //There is no way to change a crashed state to anything else
    }

    IDemoStateChangeEvent event = new IDemoStateChangeEvent(this, this.state, state);
    this.state = state;
    fireDemoStateChangeEvent(event);
  }

  public Throwable getThrowable() {
    return error;
  }

  public void addDemoStateChangeListener(IDemoStateChangeListener listener) {
    listeners.add(listener);
  }

  public void removeDemoStateChangeListener(IDemoStateChangeListener listener) {
    listeners.remove(listener);
  }

  protected void fireDemoStateChangeEvent(IDemoStateChangeEvent event) {
    List clone = new ArrayList(listeners);
    for (int i = 0; i < clone.size(); ++i) {
      ((IDemoStateChangeListener) clone.get(i)).demoStateChanged(event);
    }
  }

  public final void exit() {
    if (getState() == DemoState.FINISHED) {
      return;
    }
    try {
      demoCase.executeTearDown();
    }
    catch (Throwable e) {
      demoCrashed(e);
    }
    setState(DemoState.FINISHED);
  }

  private void setThrowable(Throwable error) {
    this.error = error;
  }

  public IDemoCase getDemo() {
    return demoCase;
  }

  public void demoCrashed(Throwable error) {
    error.printStackTrace();
    setThrowable(error);
    setState(DemoState.CRASHED);
  }

  public void showCalled() throws IllegalDemoImplementationException {
    if (isShowCalled) {
      throw new IllegalDemoImplementationException(
          getDemo().getIdentifier(),
          "Multiple calls to 'show' methods. Exactly one 'show' method must be called from within each demo method.");
    }
    isShowCalled = true;
  }

  protected void checkShowWasCalled() {
    if (!isShowCalled) {
      throw new IllegalDemoImplementationException(
          getDemo().getIdentifier(),
          "No 'show' method was called. Exactly one 'show' method must be called from within each demo method.");
    }
  }
}