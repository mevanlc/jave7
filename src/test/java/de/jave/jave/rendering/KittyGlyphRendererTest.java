package de.jave.jave.rendering;

import de.jave.ascii.plate.CharacterMetrics;
import de.jave.jave.preferences.GlyphEmulation;
import de.jave.lib.cell.Cell;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public class KittyGlyphRendererTest {
   private static final int[][] RANGES = {
      {0x2500, 0x259F}, {0x25C9, 0x25C9}, {0x25CB, 0x25CB}, {0x25CF, 0x25CF},
      {0x25D6, 0x25D7}, {0x25DC, 0x25E5}, {0x2800, 0x28FF}, {0xE0B0, 0xE0BF},
      {0xE0D6, 0xE0D7}, {0xEE00, 0xEE0B}, {0xF5D0, 0xF60D}, {0x1CC1B, 0x1CC3F},
      {0x1CD00, 0x1CDE5}, {0x1CE16, 0x1CE19}, {0x1CE51, 0x1CEAF},
      {0x1FB00, 0x1FBAE}, {0x1FBCE, 0x1FBEF}
   };
   private static final Font MONO = new Font(Font.MONOSPACED, Font.PLAIN, 18);
   private static final Font SERIF = new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 36);

   @Test
   public void coversExactlyTheAuditedKittyCodepoints() {
      Set<Integer> expected = new HashSet<>();
      for (int[] range : RANGES) {
         for (int cp = range[0]; cp <= range[1]; cp++) expected.add(cp);
      }
      assertEquals(1098, expected.size());
      assertEquals(expected, KittyGlyphRenderer.supportedCodePoints());
      for (int cp : new int[]{'A', 0x23BA, 0x25A0, 0xE0A0, 0x1FBAF, 0x1FBF0}) {
         assertFalse(Integer.toHexString(cp), KittyGlyphRenderer.supports(cp));
      }
   }

   @Test
   public void everyDrawingIsIndependentOfTheFontAndStaysInsideItsCell() {
      for (int cp : KittyGlyphRenderer.supportedCodePoints()) {
         BufferedImage a = render(cp, MONO, 32, 64);
         BufferedImage b = render(cp, SERIF, 32, 64);
         assertArrayEquals(Integer.toHexString(cp), pixels(a), pixels(b));
         int painted = 0;
         for (int y = 0; y < a.getHeight(); y++) {
            for (int x = 0; x < a.getWidth(); x++) {
               if ((a.getRGB(x, y) >>> 24) != 0) {
                  assertTrue(Integer.toHexString(cp) + " outside cell", x >= 5 && x < 37 && y >= 7 && y < 71);
                  painted++;
               }
            }
         }
         if (cp == 0x2800) assertEquals(0, painted);
         else assertTrue(Integer.toHexString(cp) + " unexpectedly blank", painted > 0);
      }
   }

   @Test
   public void builtInSelectionUsesTheFirstCodepointOfAGraphemeLikeKitty() {
      BufferedImage plain = render(0x2588, MONO, 32, 64);
      BufferedImage withSelector = render(Cell.fromText("\u2588\uFE0F").glyph(), MONO, 32, 64);
      assertArrayEquals(pixels(plain), pixels(withSelector));
   }

   @Test
   public void fractionalBlocksMeetWithoutGapsAtOddCellSizes() {
      BufferedImage image = new BufferedImage(13, 17, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics = image.createGraphics();
      try {
         graphics.setColor(Color.BLACK);
         assertTrue(KittyGlyphRenderer.draw(graphics, 0x2580, 0, 0, 13, 17));
         assertTrue(KittyGlyphRenderer.draw(graphics, 0x2584, 0, 0, 13, 17));
      } finally {
         graphics.dispose();
      }
      for (int pixel : pixels(image)) assertEquals(255, pixel >>> 24);
   }

   @Test
   public void brailleBitOrderAndSupplementaryGridMasksMatchTheirCellPositions() {
      int[][] positions = {{0, 0}, {0, 1}, {0, 2}, {1, 0}, {1, 1}, {1, 2}, {0, 3}, {1, 3}};
      for (int bit = 0; bit < positions.length; bit++) {
         BufferedImage image = render(0x2800 + (1 << bit), MONO, 32, 64);
         int x = 5 + (positions[bit][0] * 2 + 1) * 8;
         int y = 7 + (positions[bit][1] * 2 + 1) * 8;
         assertTrue((image.getRGB(x, y) >>> 24) > 0);
      }
      BufferedImage sextant = render(0x1FB00, MONO, 32, 64);
      assertTrue((sextant.getRGB(6, 8) >>> 24) > 0);
      assertEquals(0, sextant.getRGB(30, 60));
      BufferedImage octant = render(0x1CD00, MONO, 32, 64); // octant 3
      assertEquals(0, octant.getRGB(6, 8));
      assertTrue((octant.getRGB(6, 24) >>> 24) > 0);
      assertEquals(0, octant.getRGB(30, 60));
   }

   @Test
   public void javeModePreservesTheExistingRowDrawingIncludingGraphemes() {
      for (int[] row : new int[][]{{'A', 0x2500, 0x2588, 'B'}, {'A', 0x1F600, 0x1CD00}, {'A', Cell.fromText("👩‍💻").glyph(), 'B'}}) {
         BufferedImage expected = new BufferedImage(200, 80, BufferedImage.TYPE_INT_ARGB);
         BufferedImage actual = new BufferedImage(200, 80, BufferedImage.TYPE_INT_ARGB);
         Graphics2D a = expected.createGraphics();
         Graphics2D b = actual.createGraphics();
         try {
            a.setFont(MONO);
            b.setFont(MONO);
            GlyphRenderer.drawRow(a, row, 5, 50, 24);
            GlyphRenderer.drawRow(b, row, 5, 50, new CharacterMetrics(24, 60, 43), GlyphEmulation.JAVE);
         } finally {
            a.dispose();
            b.dispose();
         }
         assertArrayEquals(pixels(expected), pixels(actual));
      }
   }

   @Test
   public void mixedRowsUseTheSameGeometryAsIndividualCellsAndFallbackForOrdinaryText() {
      int[] row = {'A', 0x2588, 0x1CD00, 0x2800, 'B', 0xE0B0};
      CharacterMetrics metrics = new CharacterMetrics(24, 60, 43);
      BufferedImage expected = new BufferedImage(200, 80, BufferedImage.TYPE_INT_ARGB);
      BufferedImage actual = new BufferedImage(200, 80, BufferedImage.TYPE_INT_ARGB);
      Graphics2D a = expected.createGraphics();
      Graphics2D b = actual.createGraphics();
      try {
         a.setFont(MONO);
         b.setFont(MONO);
         for (int x = 0; x < row.length; x++) {
            if (!KittyGlyphRenderer.draw(a, row[x], 5 + x * 24, 7, 24, 60)) {
               GlyphRenderer.drawCell(a, row[x], 5, x, 50, 24);
            }
         }
         GlyphRenderer.drawRow(b, row, 5, 50, metrics, GlyphEmulation.KITTY);
      } finally {
         a.dispose();
         b.dispose();
      }
      assertArrayEquals(pixels(expected), pixels(actual));
   }

   @Test
   public void smallZoomSizesAndExistingGraphicsStateArePreserved() {
      BufferedImage image = new BufferedImage(60, 80, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics = image.createGraphics();
      try {
         graphics.setColor(new Color(10, 80, 160, 190));
         graphics.setClip(new Rectangle(3, 4, 30, 40));
         graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
         var transform = graphics.getTransform();
         var clip = graphics.getClipBounds();
         var color = graphics.getColor();
         var stroke = graphics.getStroke();
         var hints = graphics.getRenderingHints();
         for (int cp : KittyGlyphRenderer.supportedCodePoints()) {
            KittyGlyphRenderer.draw(graphics, cp, 5, 7, 1, 1);
            KittyGlyphRenderer.draw(graphics, cp, 5, 7, 3, 5);
         }
         assertEquals(transform, graphics.getTransform());
         assertEquals(clip, graphics.getClipBounds());
         assertEquals(color, graphics.getColor());
         assertEquals(stroke, graphics.getStroke());
         assertEquals(hints, graphics.getRenderingHints());
      } finally {
         graphics.dispose();
      }
   }

   private static BufferedImage render(int cp, Font font, int width, int height) {
      BufferedImage image = new BufferedImage(width + 10, height + 14, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics = image.createGraphics();
      try {
         graphics.setColor(new Color(10, 80, 160, 211));
         graphics.setFont(font);
         GlyphRenderer.drawCell(graphics, cp, 5, 0, 7 + height - 5,
            new CharacterMetrics(width, height, height - 5), GlyphEmulation.KITTY);
      } finally {
         graphics.dispose();
      }
      return image;
   }

   private static int[] pixels(BufferedImage image) {
      return image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
   }
}
