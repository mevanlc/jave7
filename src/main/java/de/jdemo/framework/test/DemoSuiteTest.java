// Copyright (c) 2008 by disy Informationssysteme GmbH
package de.jdemo.framework.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.swing.JPanel;

import de.jdemo.extensions.SwingDemoCase;
import de.jdemo.framework.DemoSuite;

import org.junit.Test;

// NOT_PUBLISHED
public class DemoSuiteTest {
  public static class OverridingDemo extends DummyPublicVoidSomethingElseAndDemo {
    @Override
    public void demo() {
      show(new JPanel());
    }
  }

  public static class DummyDemo extends SwingDemoCase {
    public void demo() {
      show(new JPanel());
    }

    public void demoTwo() {
      show(new JPanel());
    }
  }

  public static class DummyPublicVoidSomethingElseAndDemo extends SwingDemoCase {
    public void nothing() {
      show(new JPanel());
    }

    public void demo() {
      show(new JPanel());
    }
  }

  public static class DummyPrivateVoidDemo extends SwingDemoCase {
    public void demo() {
      show(new JPanel());
    }

    private void demo2() {
      show(new JPanel());
    }
  }

  public static class DummyPublicNonVoidDemo extends SwingDemoCase {
    public void demo() {
      show(new JPanel());
    }

    public boolean demo2() {
      show(new JPanel());
      return true;
    }
  }

  @Test
  public void createsDemoForEachDemoMethod() throws Exception {
    final DemoSuite suite = new DemoSuite(DummyDemo.class);
    assertThat(suite.getDemoCount(), is(2));
    assertThat(suite.getDemoAt(0).getName(), is(equalTo(DemoSuite.createDemo(
        DummyDemo.class,
        "demo").getName()))); //$NON-NLS-1$
    assertThat(suite.getDemoAt(1).getName(), is(equalTo(DemoSuite.createDemo(
        DummyDemo.class,
        "demoTwo").getName()))); //$NON-NLS-1$
  }

  @Test
  public void createsDemoForOverriddenDemoMethodsOnlyOnce() throws Exception {
    final DemoSuite suite = new DemoSuite(OverridingDemo.class);
    assertThat(suite.getDemoCount(), is(1));
    assertThat(suite.getDemoAt(0).getName(), is(equalTo(DemoSuite.createDemo(
        OverridingDemo.class,
        "demo").getName()))); //$NON-NLS-1$
  }

  @Test
  public void createsErrorDemoForNonPublicDemos() throws Exception {
    final DemoSuite suite = new DemoSuite(DummyPrivateVoidDemo.class);
    assertThat(suite.getDemoCount(), is(2));
    assertThat(suite.getDemoAt(1).getName(), is("demoError")); //$NON-NLS-1$
  }

  @Test
  public void createsDemoForVoidMethodsOnly() throws Exception {
    final DemoSuite suite = new DemoSuite(DummyPublicNonVoidDemo.class);
    assertThat(suite.getDemoCount(), is(1));
    assertThat(suite.getDemoAt(0).getName(), is(equalTo(DemoSuite.createDemo(
        DummyPublicNonVoidDemo.class,
        "demo").getName()))); //$NON-NLS-1$
  }

  @Test
  public void createsDemoForDemoMethodsOnly() throws Exception {
    final DemoSuite suite = new DemoSuite(DummyPublicVoidSomethingElseAndDemo.class);
    assertThat(suite.getDemoCount(), is(1));
    assertThat(suite.getDemoAt(0).getName(), is(equalTo(DemoSuite.createDemo(
        DummyPublicVoidSomethingElseAndDemo.class,
        "demo").getName()))); //$NON-NLS-1$
  }
}