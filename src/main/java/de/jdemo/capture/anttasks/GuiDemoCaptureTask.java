package de.jdemo.capture.anttasks;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.types.CommandlineJava;

import de.jdemo.framework.DemoIdentifier;

/**
 * @author Markus Gebhard
 */
public class GuiDemoCaptureTask extends AbstractDemoCaptureTask {
  private String imageFormat;
  private boolean includeTitle = true;
  private String lookAndFeelClassName;

  public void setLookAndFeelClassName(String lookAndFeelClassName) {
    this.lookAndFeelClassName = lookAndFeelClassName;
  }

  protected void checkParameters() {
    super.checkParameters();
  }

  public void setImageFormat(String imageFormat) {
    this.imageFormat = imageFormat;
  }

  public void setIncludeTitle(boolean includeTitle) {
    this.includeTitle = includeTitle;
  }

  protected AbstractDemoAntRunner createDemoAntRunner(DemoIdentifier demo, AntClassLoader cl) {
    return new GuiCaptureRunner(
        demo,
        cl,
        getOutputFile(),
        imageFormat,
        includeTitle,
        lookAndFeelClassName);
  }

  protected void addCommandLineArguments(CommandlineJava cmd) {
    super.addCommandLineArguments(cmd);
    if (imageFormat!=null) {
      cmd.createArgument().setValue(IAntConstants.ATTRIB_IMAGE_FORMAT + "=" + imageFormat);
    }
    cmd.createArgument().setValue(IAntConstants.ATTRIB_INCLUDE_TITLE + "=" + includeTitle);
    if (lookAndFeelClassName != null) {
      cmd.createArgument().setValue(IAntConstants.ATTRIB_LOOK_AND_FEEL_CLASS_NAME + "=" + lookAndFeelClassName);
    }
  }

  protected String getRunnerClassName() {
    return GuiCaptureRunner.class.getName();
  }
}