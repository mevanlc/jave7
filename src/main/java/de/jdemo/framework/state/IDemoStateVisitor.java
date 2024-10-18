package de.jdemo.framework.state;

/**
 * @author Markus Gebhard
 */
public interface IDemoStateVisitor {

  public void visitFinished(DemoState state);

  public void visitRunning(DemoState state);

  public void visitInitial(DemoState state);

  public void visitCrashed(DemoState state);

  public void visitStarting(DemoState state);
}
