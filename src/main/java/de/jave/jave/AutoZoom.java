package de.jave.jave;

import de.jave.ascii.plate.CharacterMetrics;
import java.awt.Dimension;
import java.awt.Font;
import java.util.function.IntFunction;

/** Chooses the largest zoom offset whose complete canvas, including its border, fits. */
final class AutoZoom {
   static final int MIN_DELTA = -10;
   static final int MAX_DELTA = 100;
   static final int CANVAS_BORDER = 4;

   private AutoZoom() {}

   static int findDelta(Font originalFont, Dimension canvasCells, Dimension available) {
      return findDelta(originalFont.getSize(), canvasCells, available,
         size -> CharacterMetrics.createCharacterMetrics(originalFont.deriveFont((float)size)));
   }

   static int findDelta(int originalFontSize, Dimension canvasCells, Dimension available, IntFunction<CharacterMetrics> measure) {
      int minimum = Math.max(MIN_DELTA, ZoomableFontModel.MIN_FONT_SIZE - originalFontSize);
      int maximum = Math.min(MAX_DELTA, ZoomableFontModel.MAX_FONT_SIZE - originalFontSize);
      // Cell metrics can be non-monotonic (notably Classic mode), so check each
      // candidate from largest to smallest instead of assuming binary search is safe.
      for (int delta = maximum; delta >= minimum; delta--) {
         CharacterMetrics metrics = measure.apply(originalFontSize + delta);
         if (fits(canvasCells, metrics, available)) {
            return delta;
         }
      }
      return minimum;
   }

   static boolean fits(Dimension canvasCells, CharacterMetrics metrics, Dimension available) {
      return (long)canvasCells.width * metrics.getWidth() + CANVAS_BORDER <= available.width
         && (long)canvasCells.height * metrics.getHeight() + CANVAS_BORDER <= available.height;
   }
}
