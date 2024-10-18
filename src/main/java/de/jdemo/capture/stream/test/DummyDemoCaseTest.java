package de.jdemo.capture.stream.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * @author Markus Gebhard
 */
public class DummyDemoCaseTest extends TestCase {

  public void testDummyDemoCaseSystemOutMethodExists() throws Exception {
    Class clazz = DummyDemoCase.class;
    clazz.getMethod("demoSystemOut" //$NON-NLS-1$
    );
  }

  public void testDummyDemoCaseSystemErrMethodExists() throws Exception {
    Class clazz = DummyDemoCase.class;
    clazz.getMethod("demoSystemErr" //$NON-NLS-1$
    );
  }

  public void testDummyDemoCaseThrowsException() throws Exception {
    Class clazz = DummyDemoCase.class;
    Object object = clazz.newInstance();
    Method method = clazz.getMethod("demoThrowsException" //$NON-NLS-1$
    );
    try {
      method.invoke(object);
    }
    catch (InvocationTargetException e) {
      assertEquals(IllegalStateException.class, e.getCause().getClass());
    }
  }
}