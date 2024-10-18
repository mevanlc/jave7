package de.jdemo.capture.anttasks;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.CommandlineJava;

import de.jdemo.capture.stream.SystemStreamType;
import de.jdemo.framework.DemoIdentifier;

/**
 * @author Markus Gebhard
 * @deprecated As of Jul 22, 2004 (Markus Gebhard) Capturing system streams is no longer supported.
 * Use {@link de.jdemo.capture.anttasks.TextDemoCaptureTask} instead.
 */
@Deprecated
public class StreamDemoCaptureTask extends AbstractDemoCaptureTask {
  private String systemStream = "out"; //$NON-NLS-1$

  public String getSystemStream() {
    return systemStream;
  }

  public void setSystemStream(String string) {
    systemStream = string;
  }

  protected void checkParameters() {
    super.checkParameters();
    if (SystemStreamType.getByName(getSystemStream()) == null) {
      throw new BuildException("Illegal value '" + getSystemStream() + "' for parameter 'systemStream'.");
    }
  }

  protected AbstractDemoAntRunner createDemoAntRunner(DemoIdentifier demo, AntClassLoader cl) {
    return new StreamCaptureRunner(demo, cl, getOutputFile(), SystemStreamType.getByName(getSystemStream()));
  }

  protected void addCommandLineArguments(CommandlineJava cmd) {
    super.addCommandLineArguments(cmd);
    cmd.createArgument().setValue(IAntConstants.ATTRIB_STREAM + "=" + getSystemStream());
  }

  protected String getRunnerClassName() {
    return StreamCaptureRunner.class.getName();
  }

  protected void execute(DemoIdentifier demoId) throws BuildException {
    log("The StreamDemoCaptureTask is deprecated - please use the TextDemoCaptureTask instead.", Project.MSG_ERR);
    super.execute(demoId);
  }
}