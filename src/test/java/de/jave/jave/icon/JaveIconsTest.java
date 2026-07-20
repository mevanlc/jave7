package de.jave.jave.icon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.junit.Test;

public class JaveIconsTest {
   private static Icon icon(int width, int height) {
      return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
   }

   @Test
   public void upscalesSquareIconSoLongestEdgeMatchesTarget() {
      Icon at24 = JaveIcons.scaleToPreferredSize(icon(16, 16), 24);
      assertEquals(24, at24.getIconWidth());
      assertEquals(24, at24.getIconHeight());

      Icon at32 = JaveIcons.scaleToPreferredSize(icon(16, 16), 32);
      assertEquals(32, at32.getIconWidth());
      assertEquals(32, at32.getIconHeight());
   }

   @Test
   public void upscalePreservesAspectRatioViaLongestEdge() {
      // camel.gif is natively 18x16
      Icon camel24 = JaveIcons.scaleToPreferredSize(icon(18, 16), 24);
      assertEquals(24, camel24.getIconWidth());
      assertEquals(21, camel24.getIconHeight());

      Icon camel32 = JaveIcons.scaleToPreferredSize(icon(18, 16), 32);
      assertEquals(32, camel32.getIconWidth());
      assertEquals(28, camel32.getIconHeight());

      // zoomplus.gif is natively 18x17
      Icon zoom24 = JaveIcons.scaleToPreferredSize(icon(18, 17), 24);
      assertEquals(24, zoom24.getIconWidth());
      assertEquals(23, zoom24.getIconHeight());

      Icon zoom32 = JaveIcons.scaleToPreferredSize(icon(18, 17), 32);
      assertEquals(32, zoom32.getIconWidth());
      assertEquals(30, zoom32.getIconHeight());
   }

   @Test
   public void defaultSizeReturnsOriginalIconUntouched() {
      Icon original = icon(16, 16);
      assertSame(original, JaveIcons.scaleToPreferredSize(original, 16));
   }

   @Test
   public void neverDownscalesLargeArtworkSharingThePath() {
      // javesplash.png (400x300), export_wizard/camelizer dialog icons (75x66),
      // and the tall tool-type strips (e.g. 9x51) must be returned untouched.
      Icon splash = icon(400, 300);
      assertSame(splash, JaveIcons.scaleToPreferredSize(splash, 32));

      Icon dialog = icon(75, 66);
      assertSame(dialog, JaveIcons.scaleToPreferredSize(dialog, 32));

      Icon toolStrip = icon(9, 51);
      assertSame(toolStrip, JaveIcons.scaleToPreferredSize(toolStrip, 32));
   }

   @Test
   public void iconAlreadyAtTargetIsReturnedUntouched() {
      Icon exact = icon(24, 24);
      assertSame(exact, JaveIcons.scaleToPreferredSize(exact, 24));
   }
}
