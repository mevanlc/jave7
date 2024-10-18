package de.jdemo.capture.text;

import java.io.Writer;

import de.jdemo.capture.DemoCaptureException;
import de.jdemo.framework.DemoCase;
import de.jdemo.framework.DemoIdentifier;
import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.IDemoCaseRunnable;
import de.jdemo.framework.ITextLauncher;
import de.jdemo.framework.state.DemoState;
import de.jdemo.framework.util.DemoUtilities;
import de.jdemo.framework.util.ErrorDemoCase;

/**
 * @author Markus Gebhard
 */
public class TextDemoCapture {

  public static void captureText(final DemoIdentifier demoId, final Writer writer) throws Throwable {
    final DemoCase demo = castToDemoCase(DemoUtilities.createDemo(demoId));
    captureText(demo, writer);
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

  public static void captureText(final DemoCase demoCase, final Writer writer) throws Throwable {
    final IDemoCaseRunnable runner = demoCase.createRunnable(true);

    final CharSequence[] launchedText = new CharSequence[1];

    final DemoCase runningDemo = (DemoCase) runner.getDemo();
    runningDemo.setTextLauncher(new ITextLauncher() {
      public void launch(final CharSequence text) {
        launchedText[0] = text;
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

    if (launchedText[0] == null) {
      throw new DemoCaptureException("Text capture failed because demo did not call a 'show' method with text.");
    }

    writer.write(launchedText[0].toString());
    writer.flush();
  }
}