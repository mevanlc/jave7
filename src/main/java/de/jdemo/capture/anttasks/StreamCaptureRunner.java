package de.jdemo.capture.anttasks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.jdemo.capture.stream.SystemStreamCapture;
import de.jdemo.capture.stream.SystemStreamType;
import de.jdemo.framework.DemoIdentifier;
import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.exceptions.DemoClassNotFoundException;
import de.jdemo.framework.exceptions.DemoMethodNotFoundException;
import de.jdemo.util.IOUtilities;

import org.apache.tools.ant.BuildException;

/**
 * @author Markus Gebhard
 * @deprecated As of Jul 22, 2004 (Markus Gebhard)
 */
@Deprecated
public class StreamCaptureRunner extends AbstractDemoAntRunner {
  private SystemStreamType stream;
  private File outputFile;

  public StreamCaptureRunner(
      DemoIdentifier demo,
      ClassLoader classLoader,
      File outputFile,
      SystemStreamType stream) {
    super(demo, classLoader);
    this.outputFile = outputFile;
    this.stream = stream;
  }

  public void run() {
    IDemoCase demo = createDemo();
    FileWriter writer;
    try {
      writer = new FileWriter(outputFile);
    }
    catch (IOException e1) {
      e1.printStackTrace();
      throw new BuildException("unable to open output file", e1);
    }

    //    log("Capturing " + type + " from '" + demoId + "' to '" + outputFile +
    // "'", Project.MSG_INFO);

    try {
      SystemStreamCapture.recordSystemStream(demo, writer, stream);
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
    finally {
      IOUtilities.close(writer);
    }
  }

  public static void main(String[] args) {
    if (args.length == 0) {
      exitWithError("required argument DemoIdentifier missing");
    }

    File outputFile = null;
    SystemStreamType streamType = null;
    for (int i = 1; i < args.length; i++) {
      if (args[i].startsWith(IAntConstants.ATTRIB_OUTPUT_FILE + "=")) {
        outputFile = new File(args[i].substring((IAntConstants.ATTRIB_OUTPUT_FILE + "=").length()));
      }
      else if (args[i].startsWith(IAntConstants.ATTRIB_STREAM + "=")) {
        String stream = args[i].substring((IAntConstants.ATTRIB_STREAM + "=").length());
        streamType = SystemStreamType.getByName(stream);
        if (streamType == null) {
          exitWithError("illegal value '" + stream + "' for system stream type.");
        }
      }
      else {
        exitWithError("argument '" + args[i] + "' not supported");
      }
    }

    if (outputFile == null) {
      exitWithError("required argument '" + IAntConstants.ATTRIB_OUTPUT_FILE + "' missing");
    }
    if (streamType == null) {
      exitWithError("required argument '" + IAntConstants.ATTRIB_STREAM + "' missing");
    }

    StreamCaptureRunner runner = new StreamCaptureRunner(
        new DemoIdentifier(args[0]),
        null,
        outputFile,
        streamType);

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