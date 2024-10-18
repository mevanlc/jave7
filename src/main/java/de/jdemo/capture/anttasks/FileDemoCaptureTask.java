package de.jdemo.capture.anttasks;

import de.jdemo.framework.DemoIdentifier;

import org.apache.tools.ant.AntClassLoader;

/**
 * @author Markus Gebhard
 */
public class FileDemoCaptureTask extends AbstractDemoCaptureTask {

  protected String getRunnerClassName() {
    return FileDemoCaptureRunner.class.getName();
  }

  protected AbstractDemoAntRunner createDemoAntRunner(DemoIdentifier demo, AntClassLoader cl) {
    return new FileDemoCaptureRunner(demo, cl, getOutputFile());
  }
}