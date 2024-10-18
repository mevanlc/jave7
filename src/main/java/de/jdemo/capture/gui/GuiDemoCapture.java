package de.jdemo.capture.gui;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import de.jave.gui.io.ImageIOUtilities;
import de.jdemo.capture.DemoCaptureException;
import de.jdemo.extensions.GuiDemoCase;
import de.jdemo.framework.DemoIdentifier;
import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.IDemoCaseRunnable;
import de.jdemo.framework.exceptions.DemoClassNotFoundException;
import de.jdemo.framework.state.DemoState;
import de.jdemo.framework.util.DemoUtilities;
import de.jdemo.framework.util.ErrorDemoCase;

/**
 * @author Markus Gebhard
 */
public class GuiDemoCapture {

  private static final long TIMEOUT = 5000;

  public BufferedImage capture(final DemoIdentifier demoId, final boolean includeFrameTitle)
      throws DemoCaptureException,
      DemoClassNotFoundException {
    final GuiDemoCase demo = castToGuiDemoCase(DemoUtilities.createDemo(demoId));
    return capture(demo, includeFrameTitle);
  }

  private static GuiDemoCase castToGuiDemoCase(final IDemoCase demo) throws DemoCaptureException {
    if (demo instanceof ErrorDemoCase) {
      final ErrorDemoCase errorDemoCase = (ErrorDemoCase) demo;
      throw new DemoCaptureException(
          "Error initializing demo (" + errorDemoCase.getException() + ").",
          errorDemoCase.getException());
    }
    if (!(demo instanceof GuiDemoCase)) {
      throw new DemoCaptureException("Demo '" + demo + "' is not a GuiDemoCase."); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return (GuiDemoCase) demo;
  }

  private BufferedImage capture(final GuiDemoCase demo, final boolean includeFrameTitle)
      throws DemoCaptureException {
    final IDemoCaseRunnable runner = demo.createRunnable(false);
    if (SwingUtilities.isEventDispatchThread()) {
      runner.run();
    }
    else {
      try {
        SwingUtilities.invokeAndWait(runner);
      }
      catch (final InterruptedException exception) {
        throw new RuntimeException("Demo '" + demo + "' has been interrupted.", exception);
      }
      catch (final InvocationTargetException exception) {
        throw new RuntimeException("Error executing Demo '" + demo + "'.", exception);
      }
    }

    if (runner.getState().equals(DemoState.CRASHED)) {
      throw new DemoCaptureException("Demo crashed before being able to take a screen capture.", //$NON-NLS-1$
          runner.getThrowable());
    }

    final GuiDemoCase guiDemo = (GuiDemoCase) runner.getDemo();
    final Window window = guiDemo.getRegisteredDemoWindow();
    if (window == null) {
      throw new RuntimeException("Demo '" + demo + "' does not have a managed window object to capture."); //$NON-NLS-1$ //$NON-NLS-2$
    }

    final long timeStart = System.currentTimeMillis();
    while (true) {
      Thread.yield();
      if (System.currentTimeMillis() - timeStart > TIMEOUT) {
        throw new DemoCaptureException("Timeout when capturing demo window.");
      }
      if (runner.getState().equals(DemoState.RUNNING) && guiDemo.isRegisteredDemoWindowOpened()) {
        final Robot robot;
        try {
          robot = new Robot();
        }
        catch (final AWTException e1) {
          throw new DemoCaptureException("Unable to create Robot for GuiDemoCapture.", //$NON-NLS-1$
              e1);
        }
        Thread.yield();
        // 14.01.2008 (Markus Gebhard): ok, so there are still paint delays and errors - but less
        // likely when we loop... Any better ideas? 
        for (int i = 0; i < 3; ++i) {
          robot.waitForIdle();
          Thread.yield();
        }
        final BufferedImage image = capture(window, includeFrameTitle);
        runner.cancel();
        return image;
      }
      if (runner.getState().equals(DemoState.CRASHED)) {
        throw new DemoCaptureException("Demo crashed before being able to create screen capture.",
        //$NON-NLS-1$
            runner.getThrowable());
      }
      if (runner.getState().equals(DemoState.FINISHED)) {
        throw new DemoCaptureException("Demo finished before being able to create screen capture.",
        //$NON-NLS-1$
            runner.getThrowable());
      }
      Thread.yield();
    }
  }

  private BufferedImage capture(final Window window, final boolean includeFrameTitle) {
    try {
      return ScreenCapture.captureFromScreen(window, includeFrameTitle);
    }
    catch (final AWTException e1) {
      e1.printStackTrace();
    }
    return null;
  }

  public void capture(
      final DemoIdentifier demoId,
      final boolean includeFrameTitle,
      final String imageFormat,
      final File file) throws DemoCaptureException, DemoClassNotFoundException, IOException {
    final BufferedImage image = capture(demoId, includeFrameTitle);
    ImageIOUtilities.write(image, imageFormat, file);
  }

  public void capture(
      final IDemoCase demo,
      final boolean includeFrameTitle,
      final String imageFormat,
      final File outputFile) throws IOException, DemoCaptureException {
    final GuiDemoCase guiDemoCase = castToGuiDemoCase(demo);
    final BufferedImage image = capture(guiDemoCase, includeFrameTitle);
    ImageIOUtilities.write(image, imageFormat, outputFile);
  }
}