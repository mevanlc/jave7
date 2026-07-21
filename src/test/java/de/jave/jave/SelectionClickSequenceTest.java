package de.jave.jave;

import org.junit.Assert;
import org.junit.Test;

public class SelectionClickSequenceTest {
   @Test
   public void sequenceWithoutSelectionSelectsCellThenFloodRegion() {
      SelectionClickSequence sequence = new SelectionClickSequence();

      sequence.start(false);

      Assert.assertTrue(sequence.selectsSingleCellAt(2));
      Assert.assertTrue(sequence.selectsFloodRegionAt(3));
   }

   @Test
   public void sequenceWithSelectionSelectsCellButNotFloodRegion() {
      SelectionClickSequence sequence = new SelectionClickSequence();

      sequence.start(true);

      Assert.assertTrue(sequence.selectsSingleCellAt(2));
      Assert.assertFalse(sequence.selectsFloodRegionAt(3));
   }

   @Test
   public void canceledSequenceDoesNotHandleMultipleClicks() {
      SelectionClickSequence sequence = new SelectionClickSequence();
      sequence.start(false);

      sequence.cancel();

      Assert.assertFalse(sequence.isActive());
      Assert.assertFalse(sequence.selectsSingleCellAt(2));
      Assert.assertFalse(sequence.selectsFloodRegionAt(3));
   }

   @Test
   public void sequenceOnlyHandlesItsExactClickCounts() {
      SelectionClickSequence sequence = new SelectionClickSequence();

      sequence.start(false);

      Assert.assertFalse(sequence.selectsSingleCellAt(1));
      Assert.assertFalse(sequence.selectsSingleCellAt(3));
      Assert.assertFalse(sequence.selectsFloodRegionAt(2));
      Assert.assertFalse(sequence.selectsFloodRegionAt(4));
   }
}
