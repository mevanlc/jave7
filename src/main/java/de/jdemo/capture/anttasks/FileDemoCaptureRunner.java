package de.jdemo.capture.anttasks;

import java.io.File;

import de.jdemo.capture.file.FileDemoCapture;
import de.jdemo.framework.DemoCase;
import de.jdemo.framework.DemoIdentifier;
import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.exceptions.DemoClassNotFoundException;
import de.jdemo.framework.exceptions.DemoMethodNotFoundException;

import org.apache.tools.ant.BuildException;

/**
 * @author Markus Gebhard
 */
public class FileDemoCaptureRunner extends AbstractDemoAntRunner {

  private File outputFile;

  public FileDemoCaptureRunner(DemoIdentifier demo, ClassLoader classLoader, File outputFile) {
    super(demo, classLoader);
    this.outputFile = outputFile;
  }

  public void run() {
    IDemoCase demo = createDemo();
    if (!(demo instanceof DemoCase)) {
      throw new BuildException("Text captures can only be performed on sublasses of " + DemoCase.class.getName());
    }
    DemoCase demoCase = (DemoCase) demo;

    try {
      FileDemoCapture.captureFile(demoCase, outputFile);
    }
    catch (DemoMethodNotFoundException e) {
      throw new BuildException("Demo method '"
          + demo.getIdentifier().getMethodName()
          + "' does not exisit in "
          + demo.getIdentifier().getClassName(), e);
    }
    catch (DemoClassNotFoundException e) {
      throw new BuildException("Demo class '" + demo.getIdentifier().getClassName() + "' not found.", e);
    }
    catch (Throwable exception) {
      throw new BuildException("Error capturing demo "
          + getDemoIdentifier().getIdentifierName()
          + " "
          + exception.getMessage(), exception);
    }
  }

  public static void main(String[] args) {
    if (args.length == 0) {
      exitWithError("required argument DemoIdentifier missing");
    }

    File outputFile = null;
    for (int i = 1; i < args.length; i++) {
      if (args[i].startsWith(IAntConstants.ATTRIB_OUTPUT_FILE + "=")) {
        outputFile = new File(args[i].substring((IAntConstants.ATTRIB_OUTPUT_FILE + "=").length()));
      }
      else {
        exitWithError("argument '" + args[i] + "' not supported");
      }
    }

    if (outputFile == null) {
      exitWithError("required argument '" + IAntConstants.ATTRIB_OUTPUT_FILE + "' missing");
    }

    FileDemoCaptureRunner runner = new FileDemoCaptureRunner(new DemoIdentifier(args[0]), null, outputFile);

    try {
      runner.run();
    }
    catch (Exception e) {
      exitWithError("Error executing demo", e);
    }
  }
}