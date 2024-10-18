package de.jdemo.framework;

/**
 * Super interface for a demo or a set of demos.
 * @see de.jdemo.framework.IDemoCase
 * @see de.jdemo.framework.IDemoSuite
 * @author Markus Gebhard
 */
public interface IDemo {
  public String getName();

  public void setName(String name);
  
  public void accept(IDemoVisitor visitor); 
}