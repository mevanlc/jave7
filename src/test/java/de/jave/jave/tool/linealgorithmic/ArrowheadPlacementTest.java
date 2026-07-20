package de.jave.jave.tool.linealgorithmic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ArrowheadPlacementTest {
   @Test
   public void cyclesThroughNoneAndAllArrowheadPlacements() {
      assertEquals(ArrowheadPlacement.NONE, ArrowheadPlacement.BOTH.next());
      assertEquals(ArrowheadPlacement.START, ArrowheadPlacement.NONE.next());
      assertEquals(ArrowheadPlacement.END, ArrowheadPlacement.START.next());
      assertEquals(ArrowheadPlacement.BOTH, ArrowheadPlacement.END.next());
   }

   @Test
   public void labelsNoArrowheadsAsNone() {
      assertEquals("None", ArrowheadPlacement.NONE.toString());
   }
}
