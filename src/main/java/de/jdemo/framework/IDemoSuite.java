package de.jdemo.framework;

/**
 * A <code>IDemoSuite</code> is a Composite of <code>IDemo</code>s.
 *
 * @author Markus Gebhard
 */
public interface IDemoSuite extends IDemo {
  /**
   * Returns the number of demos in this suite
   */
  public int getDemoCount();

  /**
   * Returns the demo at the given index
   */
  public IDemo getDemoAt(int index);
}