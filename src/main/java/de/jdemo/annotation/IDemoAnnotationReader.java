package de.jdemo.annotation;

import de.jdemo.framework.DemoIdentifier;
import de.jdemo.framework.exceptions.DemoClassNotFoundException;

/**
 * @author Markus Gebhard
 */
public interface IDemoAnnotationReader {

  /** @return the annotation to the given demo or <code>null</code> if there is none. */
  public DemoAnnotation getAnnotation(DemoIdentifier demoId) throws DemoClassNotFoundException;

}