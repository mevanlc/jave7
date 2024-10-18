package de.jdemo.framework;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import de.jdemo.framework.util.DemoInitializationException;
import de.jdemo.framework.util.DemoUtilities;
import de.jdemo.framework.util.ErrorDemoCase;

public class DemoSuite implements IDemoSuite {
  private final List<IDemo> demos = new ArrayList<IDemo>();
  private String name;

  /**
   * Constructs an empty DemoSuite.
   */
  public DemoSuite() {
    //nothing to do
  }

  /**
   * Constructs a DemoSuite from the given class with the given name.
   * @see DemoSuite#DemoSuite(Class)
   */
  public DemoSuite(final Class theClass, final String name) {
    this(theClass);
    setName(name);
  }

  /**
   * Constructs a DemoSuite from the given class. Adds all the methods
   * starting with "demo" as demo cases to the suite.
   */
  public DemoSuite(final Class theClass) {
    name = theClass.getName();
    try {
      getDemoConstructor(theClass); // Avoid generating multiple error messages
    }
    catch (final NoSuchMethodException e) {
      addDemo(createErrorDemo("Class " +
          theClass.getName() +
          " has no public constructor " +
          theClass.getName() +
          "(String name) or " +
          theClass.getName() +
          "()", e));
      return;
    }

    if (!Modifier.isPublic(theClass.getModifiers())) {
      addDemo(createErrorDemo("Class " + theClass.getName() + " is not public", null));
      return;
    }

    final List<Method> demoMethods = DemoUtilities.getDemoMethods(theClass);
    for (final Method method : demoMethods) {
      addDemoMethod(method, theClass);
    }
    if (demos.size() == 0) {
      addDemo(createErrorDemo("No demos found in " + theClass.getName(), null));
    }
  }

  /**
   * Constructs an empty DemoSuite.
   */
  public DemoSuite(final String name) {
    setName(name);
  }

  public void accept(final IDemoVisitor visitor) {
    visitor.visitDemoSuite(this);
  }

  /**
   * Adds a demo to the suite.
   */
  public void addDemo(final IDemo demo) {
    demos.add(demo);
  }

  /**
   * Adds the demos from the given class to the suite
   */
  public void addDemoSuite(final Class demoClass) {
    addDemo(new DemoSuite(demoClass));
  }

  private void addDemoMethod(final Method method, final Class theClass) {
    if (!Modifier.isPublic(method.getModifiers())) {
      addDemo(createErrorDemo(
          "Demo method '" + method.getName() + "' is not public in " + theClass,
          null));
      return;
    }
    addDemo(createDemo(theClass, method.getName()));
  }

  /**
   * ...as the moon sets over the early morning Merlin, Oregon
   * mountains, our intrepid adventurers type...
   */
  public static IDemo createDemo(final Class theClass, final String name) {
    Constructor constructor;
    try {
      constructor = getDemoConstructor(theClass);
    }
    catch (final NoSuchMethodException e) {
      return createErrorDemo("Class " +
          theClass.getName() +
          " has no public constructor " +
          theClass.getName() +
          "(String name) or " +
          theClass.getName() +
          "()", e);
    }
    IDemo demo;
    try {
      if (constructor.getParameterTypes().length == 0) {
        demo = (IDemo) constructor.newInstance(new Object[0]);
        demo.setName(name);
      }
      else {
        demo = (IDemo) constructor.newInstance(new Object[]{ name });
      }
    }
    catch (final InstantiationException e) {
      return createErrorDemo("Cannot instantiate demo case: " +
          name +
          " (" +
          exceptionToString(e) +
          ")", e);
    }
    catch (final InvocationTargetException e) {
      return createErrorDemo("Exception in constructor: " +
          name +
          " (" +
          exceptionToString(e.getTargetException()) +
          ")", e.getTargetException());
    }
    catch (final IllegalAccessException e) {
      return createErrorDemo(
          "Cannot access demo case: " + name + " (" + exceptionToString(e) + ")",
          e);
    }
    return demo;
  }

  /**
   * Converts the stack trace into a string
   */
  private static String exceptionToString(final Throwable t) {
    final StringWriter stringWriter = new StringWriter();
    final PrintWriter writer = new PrintWriter(stringWriter);
    t.printStackTrace(writer);
    return stringWriter.toString();

  }

  /**
   * Gets a constructor which takes a single String as
   * its argument or a no arg constructor.
   */
  public static Constructor getDemoConstructor(final Class theClass) throws NoSuchMethodException {
    final Class[] args = { String.class };
    try {
      return theClass.getConstructor(args);
    }
    catch (final NoSuchMethodException e) {
      // fall through
    }
    return theClass.getConstructor();
  }

  /**
   * Returns the demo at the given index
   */
  public IDemo getDemoAt(final int index) {
    return demos.get(index);
  }

  @Override
  public String toString() {
    if (getName() != null)
      return getName();
    return super.toString();
  }

  /**
   * Sets the name of the suite.
   * @param name The name to set
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * Returns the name of the suite. Not all
   * demo suites have a name and this method
   * can return <code>null</code>.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns a demo which will fail and log a warning message.
   */
  private static IDemo createErrorDemo(final String message, final Throwable cause) {
    return new ErrorDemoCase(new DemoInitializationException(message, cause));
  }

  /**
   * Returns the number of demos in this suite
   */
  public int getDemoCount() {
    return demos == null ? 0 : demos.size();
  }
}