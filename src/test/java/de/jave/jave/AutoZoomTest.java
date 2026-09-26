package de.jave.jave;

import static org.junit.Assert.*;

import de.jave.ascii.plate.CharacterMetrics;
import java.awt.Dimension;
import org.junit.Test;

public class AutoZoomTest {
   private static CharacterMetrics measure(int size) {
      return new CharacterMetrics(size, size * 2, size);
   }

   @Test public void choosesLargestFittingStepForBothAxes() {
      // Width permits size 14, but height limits this canvas to size 8.
      assertEquals(-5, find(new Dimension(40, 30), new Dimension(600, 500)));
      assertEquals(1, find(new Dimension(40, 4), new Dimension(600, 500)));
   }

   @Test public void includesCanvasBorderAndAllowsAnExactFit() {
      CharacterMetrics metrics = measure(13);
      Dimension cells = new Dimension(40, 20);
      Dimension exact = new Dimension(524, 524);
      assertTrue(AutoZoom.fits(cells, metrics, exact));
      assertFalse(AutoZoom.fits(cells, metrics, new Dimension(523, 524)));
      assertFalse(AutoZoom.fits(cells, metrics, new Dimension(524, 523)));
      assertEquals(0, find(cells, exact));
   }

   @Test public void respectsUpperAndLowerAutoZoomLimits() {
      assertEquals(100, find(new Dimension(1, 1), new Dimension(10000, 10000)));
      assertEquals(-10, find(new Dimension(10000, 10000), new Dimension(100, 100)));
   }

   @Test public void neverDerivesAZeroOrNegativeFontSize() {
      assertEquals(-5, AutoZoom.findDelta(6, new Dimension(10000, 10000), new Dimension(1, 1), size -> {
         assertTrue(size > 0);
         return measure(size);
      }));
   }

   @Test public void findsLargestFitEvenWhenCellHeightIsNonMonotonic() {
      int delta = AutoZoom.findDelta(13, new Dimension(10, 100), new Dimension(604, 500),
         size -> new CharacterMetrics(size, size >= 30 ? 1 : size * 2, size));
      assertEquals(47, delta);
   }

   @Test public void oversizedDimensionsDoNotOverflowIntoAFit() {
      assertFalse(AutoZoom.fits(new Dimension(Integer.MAX_VALUE, 1), new CharacterMetrics(8, 16, 12), new Dimension(600, 500)));
   }

   private int find(Dimension cells, Dimension available) {
      return AutoZoom.findDelta(13, cells, available, AutoZoomTest::measure);
   }
}
