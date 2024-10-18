package de.jdemo.runner;

import de.jdemo.framework.IDemo;

/**
 * @author Markus Gebhard
 */
public interface IDemoSelectionProvider {
  public IDemo getSelectedDemo();
}