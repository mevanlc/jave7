package de.jdemo.capture.gui;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import de.jdemo.extensions.IDirectImageCapturable;

public class ScreenCapture {

  public static BufferedImage captureFromScreen(Window window, boolean includeFrameTitle) throws AWTException {
    Rectangle area = null;
    if (includeFrameTitle) {
      Point location = window.getLocationOnScreen();
      Dimension size = window.getSize();
      area = new Rectangle(location.x, location.y, size.width, size.height);
    }
    else {
      IDirectImageCapturable directImageCapturable = getDirectImageCapturable(window);
      if (directImageCapturable != null) {
        return directImageCapturable.getBufferedImage();
      }
      area = getScreenAreaWithoutFrameBorder(window);
    }
    CameraRunnable camera = new CameraRunnable(area);
    if (!SwingUtilities.isEventDispatchThread()) {
      try {
        SwingUtilities.invokeAndWait(camera);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
      catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    else {
      camera.run();
    }
    return camera.getScreenShot();
  }

  private static IDirectImageCapturable getDirectImageCapturable(Window window) {
    if (window instanceof RootPaneContainer) {
      //Swing windows
      RootPaneContainer container = (RootPaneContainer) window;
      Container contentPane = container.getContentPane();
      if (contentPane.getComponentCount() == 1 && contentPane.getComponent(0) instanceof IDirectImageCapturable) {
        return (IDirectImageCapturable) contentPane.getComponent(0);
      }
    }
    else {
      //AWT windows
      if (window.getComponentCount() == 1 && window.getComponent(0) instanceof IDirectImageCapturable) {
        return (IDirectImageCapturable) window.getComponent(0);
      }
    }
    return null;
  }

  private static Rectangle getScreenAreaWithoutFrameBorder(Window window) {
    if (window instanceof RootPaneContainer) {
      //Swing windows
      RootPaneContainer container = (RootPaneContainer) window;
      Container contentPane = container.getContentPane();
      Point location = contentPane.getLocationOnScreen();
      Dimension size = contentPane.getSize();
      return new Rectangle(location.x, location.y, size.width, size.height);
    }
    else {
      //AWT windows
      Insets insets = window.getInsets();
      Point location = window.getLocationOnScreen();
      Dimension size = window.getSize();
      return new Rectangle(location.x + insets.left, location.y + insets.top, size.width
          - insets.left
          - insets.right, size.height - insets.top - insets.bottom);
    }
  }


  private static class CameraRunnable implements Runnable {
    private Rectangle area;
    private BufferedImage screenShot;
    private AWTException exception;

    public CameraRunnable(Rectangle area) {
      this.area = area;
    }

    public void run() {
      try {
        screenShot = createScreenShot(area);
      }
      catch (AWTException e) {
        this.exception = e;
      }
    }

    public BufferedImage getScreenShot() throws AWTException {
      if (exception != null) {
        throw exception;
      }
      return screenShot;
    }

    private static BufferedImage createScreenShot(Rectangle area) throws AWTException {
      Robot robot = new Robot();
      return robot.createScreenCapture(area);
    }
  }
}