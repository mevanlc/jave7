package de.jdemo.capture.anttasks;

import de.jdemo.framework.DemoIdentifier;

import org.apache.tools.ant.AntClassLoader;

/**
 * @author Markus Gebhard
 */
public class TextDemoCaptureTask extends AbstractDemoCaptureTask {

  protected String getRunnerClassName() {
    return TextDemoCaptureRunner.class.getName();
  }

  protected AbstractDemoAntRunner createDemoAntRunner(DemoIdentifier demo, AntClassLoader cl) {
    return new TextDemoCaptureRunner(demo, cl, getOutputFile());
  }

}