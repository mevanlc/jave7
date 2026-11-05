package de.jave.jave.rendering;

import de.jave.ascii.plate.CharacterMetrics;
import de.jave.jave.preferences.GlyphEmulation;
import de.jave.lib.cell.Cell;
import de.jave.lib.cell.GlyphEncoding;
import java.awt.Graphics;

public final class GlyphRenderer {
   private GlyphRenderer() {
   }

   public static int cellX(int originX, int column, int cellWidth) {
      return originX + column * cellWidth;
   }

   public static void drawRow(Graphics graphics, int[] glyphs, int originX, int baselineY, int cellWidth) {
      if (allBmpCodePoints(glyphs)) {
         graphics.drawString(new String(glyphs, 0, glyphs.length), originX, baselineY);
         return;
      }

      for (int x = 0; x < glyphs.length; x++) {
         drawCell(graphics, glyphs[x], originX, x, baselineY, cellWidth);
      }
   }

   public static void drawCell(Graphics graphics, int glyph, int originX, int column, int baselineY, int cellWidth) {
      graphics.drawString(new Cell(glyph).text(), cellX(originX, column, cellWidth), baselineY);
   }

   public static void drawRow(Graphics graphics, int[] glyphs, int originX, int baselineY, CharacterMetrics metrics, GlyphEmulation emulation) {
      if (emulation == GlyphEmulation.JAVE) {
         drawRow(graphics, glyphs, originX, baselineY, metrics.getWidth());
         return;
      }
      for (int column = 0; column < glyphs.length; column++) {
         drawCell(graphics, glyphs[column], originX, column, baselineY, metrics, emulation);
      }
   }

   public static void drawCell(Graphics graphics, int glyph, int originX, int column, int baselineY, CharacterMetrics metrics, GlyphEmulation emulation) {
      int x = cellX(originX, column, metrics.getWidth());
      if (emulation == GlyphEmulation.KITTY) {
         int codePoint = GlyphEncoding.isCluster(glyph) ? new Cell(glyph).text().codePointAt(0) : glyph;
         if (KittyGlyphRenderer.draw(graphics, codePoint, x, baselineY - metrics.getAscent(), metrics.getWidth(), metrics.getHeight())) {
            return;
         }
      }
      drawCell(graphics, glyph, originX, column, baselineY, metrics.getWidth());
   }

   private static boolean allBmpCodePoints(int[] glyphs) {
      for (int glyph : glyphs) {
         if (!GlyphEncoding.isCodePoint(glyph) || glyph > Character.MAX_VALUE || Character.isSurrogate((char)glyph)) {
            return false;
         }
      }
      return true;
   }
}
