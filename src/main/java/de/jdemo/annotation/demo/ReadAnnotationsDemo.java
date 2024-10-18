package de.jdemo.annotation.demo;

import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JLabel;

import de.jdemo.annotation.DemoAnnotation;
import de.jdemo.annotation.IDemoAnnotationReader;
import de.jdemo.annotation.DemoAnnotationReader;
import de.jdemo.extensions.SwingDemoCase;
import de.jdemo.framework.DemoIdentifier;
import de.jdemo.framework.exceptions.DemoClassNotFoundException;

/**
 * @author Markus Gebhard
 */
public class ReadAnnotationsDemo extends SwingDemoCase {

  public void demoAnnotatedHelloWorld() throws DemoClassNotFoundException {
    showAnnotations(new DemoIdentifier(
        "de.jdemo.examples.annotation.AnnotationExampleDemo:demoAnnotatedHelloWorld"));
  }

  public void demoAnnotatedHelloWorld2() throws DemoClassNotFoundException {
    showAnnotations(new DemoIdentifier(
        "de.jdemo.examples.annotation.AnnotationExampleDemo:demoAnnotatedHelloWorld2"));
  }

  private void showAnnotations(final DemoIdentifier identifier) throws DemoClassNotFoundException {
    final IDemoAnnotationReader reader = new DemoAnnotationReader();
    final DemoAnnotation annotation = reader.getAnnotation(identifier);
    final JLabel descriptionLabel = new JLabel(annotation.getDescription());
    final JLabel categoriesLabel = new JLabel(Arrays.asList(annotation.getCategories()).toString());
    show(new JComponent[]{ descriptionLabel, categoriesLabel }, new GridLayout(0, 1));
  }
}