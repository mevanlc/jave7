package de.jave.ascii.plate;

import java.awt.Font;
import java.awt.FontMetrics;

/**
 * How a cell's height is derived from the rendering font.
 *
 * Width is always {@code FontMetrics.stringWidth("#")}, which scales linearly
 * with font size. The modes differ only in how cell height (and the baseline
 * ascent) are computed, which in turn controls the cell aspect ratio at all
 * zoom levels.
 */
public enum CellScalingMode {
   /**
    * Original empirical formula: {@code -0.14*s^2 + 4.56*s - 19.3}.
    *
    * Hand-fit for font sizes ~6..16 (the historical zoom range). Above ~16
    * the parabola turns over and cells start getting wider than tall; above
    * ~28 the formula returns negative values.
    */
   CLASSIC("Classic") {
      @Override
      int width(Font font, FontMetrics fm) {
         return fm.stringWidth("#");
      }

      @Override
      int height(Font font, FontMetrics fm) {
         double s = font.getSize();
         return (int)Math.round(-0.14 * s * s + 4.56 * s - 19.3);
      }

      @Override
      int ascent(Font font, FontMetrics fm) {
         return height(font, fm) - fm.getDescent();
      }
   },

   /**
    * Full {@code FontMetrics.getHeight()} — ascent + descent + leading.
    *
    * Matches what most terminals do; gives roomy rows that always preserve
    * the font's natural aspect ratio at every zoom level.
    */
   LINE("Line") {
      @Override
      int width(Font font, FontMetrics fm) {
         return fm.stringWidth("#");
      }

      @Override
      int height(Font font, FontMetrics fm) {
         return fm.getHeight();
      }

      @Override
      int ascent(Font font, FontMetrics fm) {
         return fm.getAscent();
      }
   },

   /**
    * Glyph box only ({@code ascent + descent}, no leading).
    *
    * Tighter rows than {@link #LINE}; keeps proper aspect at all sizes.
    */
   TIGHT("Tight") {
      @Override
      int width(Font font, FontMetrics fm) {
         return fm.stringWidth("#");
      }

      @Override
      int height(Font font, FontMetrics fm) {
         return fm.getAscent() + fm.getDescent();
      }

      @Override
      int ascent(Font font, FontMetrics fm) {
         return fm.getAscent();
      }
   },

   /**
    * User-defined scale factors applied to the natural glyph metrics:
    * width = scaleW * stringWidth("#"), height = scaleH * (ascent + descent).
    *
    * Scales come from {@link CharacterMetrics#getWidthScale()} /
    * {@link CharacterMetrics#getHeightScale()}.
    */
   SCALED("Scaled") {
      @Override
      int width(Font font, FontMetrics fm) {
         return Math.round(CharacterMetrics.getWidthScale() * fm.stringWidth("#"));
      }

      @Override
      int height(Font font, FontMetrics fm) {
         return Math.round(CharacterMetrics.getHeightScale() * (fm.getAscent() + fm.getDescent()));
      }

      @Override
      int ascent(Font font, FontMetrics fm) {
         return fm.getAscent();
      }
   };

   private final String displayName;

   CellScalingMode(String displayName) {
      this.displayName = displayName;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   abstract int width(Font font, FontMetrics fm);

   abstract int height(Font font, FontMetrics fm);

   abstract int ascent(Font font, FontMetrics fm);

   public static CellScalingMode fromName(String name, CellScalingMode fallback) {
      if (name == null) {
         return fallback;
      }
      try {
         return valueOf(name);
      } catch (IllegalArgumentException e) {
         return fallback;
      }
   }
}
