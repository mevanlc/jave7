package de.jdemo.framework;

/**
 * @author Markus Gebhard
 */
public interface IDemoVisitor {

  public void visitDemoCase(IDemoCase demoCase);

  public void visitDemoSuite(IDemoSuite demoSuite);
}
