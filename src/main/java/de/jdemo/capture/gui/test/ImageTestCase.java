package de.jdemo.capture.gui.test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import junit.framework.TestCase;

/**
 * @author Markus Gebhard
 */
public abstract class ImageTestCase extends TestCase {

  public static void assertEquals(BufferedImage expected, BufferedImage actual, int delta) {
    assertEquals(expected.getWidth(), actual.getWidth());
    assertEquals(expected.getHeight(), actual.getHeight());
    int width = expected.getWidth();
    int height = expected.getHeight();
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        Color expectedColor = new Color(expected.getRGB(x, y));
        Color actualColor = new Color(actual.getRGB(x, y));
        assertEquals(expectedColor, actualColor, delta);
      }
    }
  }

  private static void assertEquals(Color expected, Color actual, int delta) {
    assertEquals(expected.getRed(), actual.getRed(), delta);
    assertEquals(expected.getGreen(), actual.getGreen(), delta);
    assertEquals(expected.getBlue(), actual.getBlue(), delta);
    assertEquals(expected.getAlpha(), actual.getAlpha(), delta);
  }

  private static void assertEquals(int expected, int actual, int delta) {
    assertTrue("Expected: "+expected+", was:"+actual, Math.abs(actual - expected) <= delta); //$NON-NLS-1$ //$NON-NLS-2$
  }
}