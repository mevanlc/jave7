package de.jdemo.runner;

import de.jdemo.framework.DemoIdentifier;

/**
 * @author Markus Gebhard
 */
public interface IDemoIdentifierSelectionProvider {
  public DemoIdentifier getSelectedDemoIdentifier();
}
