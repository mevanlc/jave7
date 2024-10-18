package de.jdemo.framework.util.test;

import junit.framework.TestCase;
import de.jdemo.framework.IDemo;
import de.jdemo.framework.IDemoVisitor;
import de.jdemo.framework.util.DemoInitializationException;
import de.jdemo.framework.util.DemoUtilities;
import de.jdemo.framework.util.ErrorDemoCase;

/**
 * @author Markus Gebhard
 */
public class DemoUtilitiesTest extends TestCase {

  public void testGetDisplayName() {
    assertEquals("Hello World", DemoUtilities.getDisplayName(createDummyDemo("demoHelloWorld"))); //$NON-NLS-1$ //$NON-NLS-2$
    assertNotNull(DemoUtilities.getDisplayName(createDummyDemo("demo"))); //$NON-NLS-1$
    assertEquals("Is 42 Number", DemoUtilities.getDisplayName(createDummyDemo("demoIs42Number"))); //$NON-NLS-1$ //$NON-NLS-2$
    assertEquals("1 Is A 1", DemoUtilities.getDisplayName(createDummyDemo("demo1IsA1"))); //$NON-NLS-1$ //$NON-NLS-2$
  }

  private IDemo createDummyDemo(final String name) {
    return new IDemo() {
      public String getName() {
        return name;
      }
      public void setName(String name) {
        //nothing to do
      }
      public void accept(IDemoVisitor visitor) {
        //nothing to do
      }
    };
  }
  
  public void testInstantiatingDemoClassNotBeingDemoCaseImplementation() {
    ErrorDemoCase error = (ErrorDemoCase) DemoUtilities.createDemoCase("demo", NonDemo.class);
    DemoInitializationException exception = (DemoInitializationException) error.getException();
    assertNull(exception.getCause());
  }
  
  public final static class NonDemo{
    //nothing to do
  }
}