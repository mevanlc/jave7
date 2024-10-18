package de.jdemo.capture.gui.test;

import java.awt.image.BufferedImage;
import java.io.File;

import de.jdemo.capture.DemoCaptureException;
import de.jdemo.capture.gui.GuiDemoCapture;
import de.jdemo.framework.DemoIdentifier;
import de.jdemo.framework.exceptions.DemoClassNotFoundException;

/**
 * @author Markus Gebhard
 */
public class GuiDemoCaptureTest extends ImageTestCase {
  private static final int LOOP_COUNT = 25;
  private final static int COLOR_DELTA = 0;

  private final static DemoIdentifier COMPLEX_DEMO_ID = new DemoIdentifier(ScreenCaptureTestSwingDemo.class
      .getName(), "demoComplex"); //$NON-NLS-1$ //$NON-NLS-2$

  private final static DemoIdentifier MEDIUM_DEMO_ID = new DemoIdentifier(ScreenCaptureTestSwingDemo.class
      .getName(), "demoMedium"); //$NON-NLS-1$ //$NON-NLS-2$

  private final static DemoIdentifier SIMPLE_DEMO_ID = new DemoIdentifier(ScreenCaptureTestSwingDemo.class
      .getName(), "demoSimple"); //$NON-NLS-1$ //$NON-NLS-2$

  private final static DemoIdentifier[] ALL_DEMO_IDS = new DemoIdentifier[]{
      COMPLEX_DEMO_ID,
      MEDIUM_DEMO_ID,
      SIMPLE_DEMO_ID, };

  /* Smoke test */
  public void testSwingCapture() throws Throwable {
    DemoIdentifier id = new DemoIdentifier(SwingTestDemo.class.getName(), "demo"); //$NON-NLS-1$
    new GuiDemoCapture().capture(id, true, "PNG", File.createTempFile("swingDemoComponentWithTitle", ".png")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    new GuiDemoCapture().capture(id, false, "PNG", File.createTempFile("swingDemoComponentWithoutTitle", ".png")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  /* Smoke test */
  public void testAwtCapture() throws Throwable {
    DemoIdentifier id = new DemoIdentifier(AwtTestDemo.class.getName(), "demo"); //$NON-NLS-1$
    new GuiDemoCapture().capture(id, true, "PNG", File.createTempFile("awtDemoComponentWithTitle", ".png")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    new GuiDemoCapture().capture(id, false, "PNG", File.createTempFile("swingDemoComponentWithoutTitle", ".png")); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
  }

  public void testSwingSimpleCapture() throws DemoCaptureException, DemoClassNotFoundException {
    DemoIdentifier id = SIMPLE_DEMO_ID;

    BufferedImage image1 = new GuiDemoCapture().capture(id, false);
    for (int i = 0; i < LOOP_COUNT; ++i) {
      BufferedImage image2 = new GuiDemoCapture().capture(id, false);
      assertEquals(image1, image2, COLOR_DELTA);
    }
  }

  public void testSwingMediumCapture() throws DemoCaptureException, DemoClassNotFoundException {
    DemoIdentifier id = MEDIUM_DEMO_ID;

    BufferedImage image1 = new GuiDemoCapture().capture(id, false);
    for (int i = 0; i < LOOP_COUNT; ++i) {
      BufferedImage image2 = new GuiDemoCapture().capture(id, false);
      assertEquals(image1, image2, COLOR_DELTA);
    }
  }

  public void testSwingComplexCapture() throws DemoCaptureException, DemoClassNotFoundException {
    DemoIdentifier id = COMPLEX_DEMO_ID;

    BufferedImage image1 = new GuiDemoCapture().capture(id, false);
    for (int i = 0; i < LOOP_COUNT; ++i) {
      BufferedImage image2 = new GuiDemoCapture().capture(id, false);
      assertEquals(image1, image2, COLOR_DELTA);
    }
  }

  public void testCaptureAllDemosRandomOrder() throws DemoCaptureException, DemoClassNotFoundException {
    for (int i = 0; i < LOOP_COUNT; ++i) {
      DemoIdentifier id = getRandomDemoId();
      BufferedImage image1 = new GuiDemoCapture().capture(id, false);
      BufferedImage image2 = new GuiDemoCapture().capture(id, false);
      assertEquals(image1, image2, COLOR_DELTA);
    }
  }

  private DemoIdentifier getRandomDemoId() {
    int index = (int) (Math.random() * ALL_DEMO_IDS.length);
    return ALL_DEMO_IDS[index];
  }

}