package de.jdemo.capture.file;

import java.io.File;

import de.jdemo.capture.DemoCaptureException;
import de.jdemo.framework.DemoCase;
import de.jdemo.framework.DemoIdentifier;
import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.IDemoCaseRunnable;
import de.jdemo.framework.state.DemoState;
import de.jdemo.framework.util.DemoUtilities;
import de.jdemo.framework.util.ErrorDemoCase;
import de.jdemo.util.IFileLauncher;
import de.jdemo.util.IOUtilities;

/**
 * @author Markus Gebhard
 */
public class FileDemoCapture {

  public static void captureFile(final DemoIdentifier demoId, final File outputFile) throws Throwable {
    final DemoCase fileDemo = castToDemoCase(DemoUtilities.createDemo(demoId));
    captureFile(fileDemo, outputFile);
  }

  private static DemoCase castToDemoCase(final IDemoCase demo) throws DemoCaptureException {
    if (demo instanceof ErrorDemoCase) {
      final ErrorDemoCase errorDemoCase = (ErrorDemoCase) demo;
      throw new DemoCaptureException(
          "Error initializing demo (" + errorDemoCase.getException() + ").",
          errorDemoCase.getException());
    }
    if (!(demo instanceof DemoCase)) {
      throw new DemoCaptureException("File captures can only be performed on sublasses of "
          + DemoCase.class.getName());
    }
    return (DemoCase) demo;
  }

  public static void captureFile(final DemoCase demoCase, final File outputFile) throws Throwable {
    final IDemoCaseRunnable runner = demoCase.createRunnable(true);

    final File[] launchedFile = new File[1];

    final DemoCase runningDemo = (DemoCase) runner.getDemo();
    runningDemo.setFileLauncher(new IFileLauncher() {
      public void launch(final File file) {
        launchedFile[0] = file;
      }
    });

    final Thread thread = new Thread(runner);
    thread.start();
    try {
      thread.join();
    }
    catch (final InterruptedException e) {
      //nothing to do
    }
    if (runner.getState().equals(DemoState.CRASHED)) {
      throw runner.getThrowable();
    }

    if (launchedFile[0] == null) {
      throw new DemoCaptureException("File capture failed because demo did not call a 'show' method with a file.");
    }

    IOUtilities.copy(launchedFile[0], outputFile);
  }
}