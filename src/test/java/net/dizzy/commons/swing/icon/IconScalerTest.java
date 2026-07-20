package net.dizzy.commons.swing.icon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.junit.Test;

public class IconScalerTest {
   private static Icon icon(int width, int height) {
      return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
   }

   @Test
   public void upscalesSquareIconSoLongestEdgeMatchesTarget() {
      Icon at24 = IconScaler.scaleToPreferredSize(icon(16, 16), 24);
      assertEquals(24, at24.getIconWidth());
      assertEquals(24, at24.getIconHeight());

      Icon at32 = IconScaler.scaleToPreferredSize(icon(16, 16), 32);
      assertEquals(32, at32.getIconWidth());
      assertEquals(32, at32.getIconHeight());
   }

   @Test
   public void upscalePreservesAspectRatioViaLongestEdge() {
      // camel.gif is natively 18x16
      Icon camel24 = IconScaler.scaleToPreferredSize(icon(18, 16), 24);
      assertEquals(24, camel24.getIconWidth());
      assertEquals(21, camel24.getIconHeight());

      Icon camel32 = IconScaler.scaleToPreferredSize(icon(18, 16), 32);
      assertEquals(32, camel32.getIconWidth());
      assertEquals(28, camel32.getIconHeight());

      // zoomplus.gif is natively 18x17
      Icon zoom24 = IconScaler.scaleToPreferredSize(icon(18, 17), 24);
      assertEquals(24, zoom24.getIconWidth());
      assertEquals(23, zoom24.getIconHeight());

      Icon zoom32 = IconScaler.scaleToPreferredSize(icon(18, 17), 32);
      assertEquals(32, zoom32.getIconWidth());
      assertEquals(30, zoom32.getIconHeight());
   }

   @Test
   public void baseSizeReturnsOriginalIconUntouched() {
      Icon original = icon(16, 16);
      assertSame(original, IconScaler.scaleToPreferredSize(original, 16));
   }

   @Test
   public void neverDownscalesLargeArtworkSharingThePath() {
      // javesplash.png (400x300), export-wizard/camelizer/quick-start dialog icons (75x66),
      // and the quick-start item icons (95x72) must be returned untouched.
      Icon splash = icon(400, 300);
      assertSame(splash, IconScaler.scaleToPreferredSize(splash, 32));

      Icon dialog = icon(75, 66);
      assertSame(dialog, IconScaler.scaleToPreferredSize(dialog, 32));

      Icon quickStartItem = icon(95, 72);
      assertSame(quickStartItem, IconScaler.scaleToPreferredSize(quickStartItem, 32));
   }

   @Test
   public void leavesElongatedSmallArtworkUntouched() {
      // Small but far-from-square art (e.g. a tall tool-type strip) is left alone rather than
      // stretched, even though its longest edge is below the target.
      Icon tallStrip = icon(9, 20);
      assertSame(tallStrip, IconScaler.scaleToPreferredSize(tallStrip, 24));

      Icon wideStrip = icon(20, 8);
      assertSame(wideStrip, IconScaler.scaleToPreferredSize(wideStrip, 24));
   }

   @Test
   public void scaledIconDisplaysEnlargedButCarriesNativeBaseForMenus() {
      Icon original = icon(16, 16);
      Icon scaled = IconScaler.scaleToPreferredSize(original, 32);

      // Toolbars/tabs/palettes paint it at the enlarged size...
      assertEquals(32, scaled.getIconWidth());
      assertEquals(32, scaled.getIconHeight());

      // ...while menus can recover the native 16px artwork via the base-icon hook.
      assertTrue(scaled instanceof IBaseIconProvider);
      Icon base = ((IBaseIconProvider)scaled).getBaseIcon();
      assertSame(original, base);
      assertEquals(16, base.getIconWidth());
      assertEquals(16, base.getIconHeight());
   }

   @Test
   public void untouchedIconCarriesNoSeparateBase() {
      // When nothing is rescaled there is no base to swap in; menus render the icon as-is.
      Icon original = icon(16, 16);
      assertFalse(IconScaler.scaleToPreferredSize(original, 16) instanceof IBaseIconProvider);

      Icon large = icon(75, 66);
      assertFalse(IconScaler.scaleToPreferredSize(large, 32) instanceof IBaseIconProvider);
   }

   @Test
   public void iconAlreadyAtTargetIsReturnedUntouched() {
      Icon exact = icon(24, 24);
      assertSame(exact, IconScaler.scaleToPreferredSize(exact, 24));
   }

   @Test
   public void nonImageIconIsReturnedUntouched() {
      Icon plain = new Icon() {
         @Override
         public int getIconWidth() {
            return 16;
         }

         @Override
         public int getIconHeight() {
            return 16;
         }

         @Override
         public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
         }
      };
      assertSame(plain, IconScaler.scaleToPreferredSize(plain, 32));
   }
}
