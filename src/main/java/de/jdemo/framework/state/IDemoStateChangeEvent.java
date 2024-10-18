package de.jdemo.framework.state;

import java.util.EventObject;

/**
 * @author Markus Gebhard
 */
public class IDemoStateChangeEvent extends EventObject {
  private DemoState oldState;
  private DemoState newState;

  public IDemoStateChangeEvent(Object source, DemoState oldState, DemoState newState) {
    super(source);
    this.oldState = oldState;
    this.newState = newState;
  }

  public DemoState getNewState() {
    return newState;
  }

  public DemoState getOldState() {
    return oldState;
  }
}
