package de.jdemo.capture.anttasks;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import de.jdemo.capture.gui.GuiDemoCapture;
import de.jdemo.extensions.SwingDemoCase;
import de.jdemo.framework.DemoIdentifier;
import de.jdemo.framework.IDemoCase;

/**
 * @author Markus Gebhard
 */
public class GuiCaptureRunner extends AbstractDemoAntRunner {
  private boolean isIncludeTitle;
  private String imageFormat;
  private File outputFile;
  private String lookAndFeelClassName;

  public GuiCaptureRunner(
      DemoIdentifier demo,
      ClassLoader classLoader,
      File outputFile,
      String imageFormat,
      boolean isIncludeTitle,
      String lookAndFeelClassName) {
    super(demo, classLoader);
    this.outputFile = outputFile;
    this.imageFormat = imageFormat;
    this.isIncludeTitle = isIncludeTitle;
    this.lookAndFeelClassName = lookAndFeelClassName;
  }

  public void run() {
    IDemoCase demo = createDemo();
    try {
      if (lookAndFeelClassName != null && demo instanceof SwingDemoCase) {
        ((SwingDemoCase) demo).setConfiguredLookAndFeelClassName(lookAndFeelClassName);
      }

      new GuiDemoCapture().capture(demo, isIncludeTitle, imageFormat, outputFile);
    }
    catch (Exception exception) {
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
    String imageFormat = null;
    String lookAndFeelClassName = null;
    boolean includeTitle = false;
    for (int i = 1; i < args.length; i++) {
      if (args[i].startsWith(IAntConstants.ATTRIB_OUTPUT_FILE + "=")) {
        outputFile = new File(args[i].substring((IAntConstants.ATTRIB_OUTPUT_FILE + "=").length()));
      }
      else if (args[i].startsWith(IAntConstants.ATTRIB_IMAGE_FORMAT + "=")) {
        imageFormat = args[i].substring((IAntConstants.ATTRIB_STREAM + "=").length());
      }
      else if (args[i].startsWith(IAntConstants.ATTRIB_INCLUDE_TITLE + "=")) {
        includeTitle = Project.toBoolean(args[i].substring((IAntConstants.ATTRIB_INCLUDE_TITLE + "=").length()));
      }
      else if (args[i].startsWith(IAntConstants.ATTRIB_LOOK_AND_FEEL_CLASS_NAME + "=")) {
        lookAndFeelClassName = args[i].substring((IAntConstants.ATTRIB_LOOK_AND_FEEL_CLASS_NAME + "=").length());
      }
      else {
        exitWithError("argument '" + args[i] + "' not supported");
      }
    }

    if (outputFile == null) {
      exitWithError("required argument '" + IAntConstants.ATTRIB_OUTPUT_FILE + "' missing");
    }
    GuiCaptureRunner runner = new GuiCaptureRunner(
        new DemoIdentifier(args[0]),
        null,
        outputFile,
        imageFormat,
        includeTitle,
        lookAndFeelClassName);

    //    // Add/overlay system properties on the properties from the Ant project
    //    Hashtable p = System.getProperties();
    //    for (Enumeration enum = p.keys(); enum.hasMoreElements();) {
    //      Object key = enum.nextElement();
    //      props.put(key, p.get(key));
    //    }
    //    t.setProperties(props);
    //    runner.forked = true;
    //    transferFormatters(runner);
    try {
      runner.run();
    }
    catch (Exception e) {
      exitWithError("Error executing demo", e);
    }
  }
}