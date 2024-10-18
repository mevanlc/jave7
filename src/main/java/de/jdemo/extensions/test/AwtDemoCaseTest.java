package de.jdemo.extensions.test;

import java.awt.Button;

import de.jdemo.extensions.AwtDemoCase;
import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.test.AbstractDemoCaseTest;

/**
 * @author Markus Gebhard
 */
public class AwtDemoCaseTest extends AbstractDemoCaseTest {

  protected IDemoCase createEmptyDemo() {
    return new AwtDemoCase("demo") {
      //empty
    };
  }

  protected IDemoCase createCrashingDemo(final RuntimeException exception) {
    return new AwtDemoCase("demo") {
      public void demo() {
        throw exception;
      }
    };
  }

  protected IDemoCase createValidSelfExitingDemo() {
    return new AwtDemoCase("demo") {
      public void demo() {
        show(new Button("hello world"));
        exit();
      }
    };
  }
}