package de.jave.jave.tool.text;

import de.jave.ascii.plate.CharacterMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;
import org.junit.Assert;
import org.junit.Test;

public class JaveTextCursorTest {
   @Test
   public void dirtyBoundsCoverAllCursorPixelsIncludingSmallZoomLevels() throws Exception {
      SwingUtilities.invokeAndWait(() -> {
         for (TextCursorStyle style : TextCursorStyle.values()) {
            for (int width : new int[]{1, 8, 24}) {
               for (int height : new int[]{1, 2, 16, 40}) {
                  JaveTextCursor cursor = new JaveTextCursor(new Point(10, 10), style, new CharacterMetrics(width, height, height));
                  Rectangle bounds = cursor.getBounds();
                  BufferedImage image = new BufferedImage(64, 80, BufferedImage.TYPE_INT_RGB);
                  Graphics2D graphics = image.createGraphics();
                  try {
                     cursor.paintXor(graphics);
                  } finally {
                     graphics.dispose();
                  }
                  int changedPixels = 0;
                  for (int y = 0; y < image.getHeight(); y++) {
                     for (int x = 0; x < image.getWidth(); x++) {
                        if ((image.getRGB(x, y) & 0xFFFFFF) != 0) {
                           changedPixels++;
                           Assert.assertTrue(style + " pixel outside repaint bounds: " + x + "," + y, bounds.contains(x, y));
                        }
                     }
                  }
                  Assert.assertTrue("Cursor should paint pixels: " + style, changedPixels > 0);
               }
            }
         }
      });
   }
}
