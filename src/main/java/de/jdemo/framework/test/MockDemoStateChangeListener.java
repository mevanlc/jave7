package de.jdemo.framework.test;

import junit.framework.Assert;
import de.jdemo.framework.state.DemoState;
import de.jdemo.framework.state.IDemoStateChangeEvent;
import de.jdemo.framework.state.IDemoStateChangeListener;
import de.jdemo.framework.state.IDemoStateVisitor;

/**
 * @author Markus Gebhard
 */
public class MockDemoStateChangeListener implements IDemoStateChangeListener {
  private StringBuffer stateChanges = new StringBuffer();
  
  public final static String VALID_DEMO_STATE_CHANGES="is|sr|rf|";
  public final static String CRASHING_INSIDE_DEMO_METHOD_CHANGES = "is|sr|rc|";
  
  private IDemoStateVisitor visitor = new IDemoStateVisitor() {
    public void visitFinished(DemoState state) {
      stateChanges.append('f');
    }
    public void visitRunning(DemoState state) {
      stateChanges.append('r');
    }
    public void visitInitial(DemoState state) {
      stateChanges.append('i');
    }
    public void visitCrashed(DemoState state) {
      stateChanges.append('c');
    }
    public void visitStarting(DemoState state) {
      stateChanges.append('s');
    }
  };

  
  public void demoStateChanged(IDemoStateChangeEvent event) {
    event.getOldState().accept(visitor);
    event.getNewState().accept(visitor);
    stateChanges.append('|');
  }

  public void verify(String pattern) {
    Assert.assertEquals(pattern, stateChanges.toString());
  }
}