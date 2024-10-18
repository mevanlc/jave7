package de.jdemo.capture.anttasks;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;

import de.jdemo.framework.DemoIdentifier;
import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.util.DemoUtilities;

/**
 * @author Markus Gebhard
 */
public abstract class AbstractDemoAntRunner {
  public static final int ERRORS = 2;
  public static final int SUCCESS = 0;
  private ClassLoader classLoader;
  private DemoIdentifier demoId;

  public AbstractDemoAntRunner(DemoIdentifier demoId, ClassLoader classLoader) {
    this.demoId = demoId;
    this.classLoader = classLoader;
  }

  public abstract void run();

  protected DemoIdentifier getDemoIdentifier() {
    return demoId;
  }

  protected IDemoCase createDemo() throws BuildException {
    try {
      Class demoClass = null;
      if (classLoader == null) {
        demoClass = Class.forName(demoId.getClassName());
      }
      else {
        demoClass = classLoader.loadClass(demoId.getClassName());
        AntClassLoader.initializeClass(demoClass);
      }

      //      Method suiteMethod = null;
      //      try {
      //        // check if there is a suite method
      //        suiteMethod = demoClass.getMethod("suite", new Class[0]);
      //      } catch (Exception e) {
      //        // no appropriate suite method found. We don't report any
      //        // error here since it might be perfectly normal. We don't
      //        // know exactly what is the cause, but we're doing exactly
      //        // the same as JUnit TestRunner do. We swallow the exceptions.
      //      }
      //      if (suiteMethod != null){
      //        // if there is a suite method available, then try
      //        // to extract the suite from it. If there is an error
      //        // here it will be caught below and reported.
      //        suite = (Test) suiteMethod.invoke(null, new Class[0]);
      //      } else {
      //        // try to extract a test suite automatically
      //        // this will generate warnings if the class is no suitable Test
      //        suite = new TestSuite(demoClass);
      //      }
      //      
      return DemoUtilities.createDemoCase(demoId.getMethodName(), demoClass);
    }
    catch (Exception exception) {
      throw new BuildException("unable to create demo (" + exception.getMessage() + ")", exception);
    }
  }

  protected static void exitWithError(String message) {
    exitWithError(message, null);
  }

  protected static void exitWithError(String message, Exception e) {
    System.err.println(message);
    if (e != null) {
      e.printStackTrace();
    }
    System.exit(ERRORS);
  }
}