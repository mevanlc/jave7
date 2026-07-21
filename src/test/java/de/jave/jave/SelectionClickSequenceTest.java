package de.jave.jave;

import java.awt.Point;
import org.junit.Assert;
import org.junit.Test;

public class SelectionClickSequenceTest {
   @Test
   public void sameCellWithinIntervalCompletesDoubleClickAcrossTools() {
      SelectionClickSequence sequence = new SelectionClickSequence(500L);
      Point location = new Point(4, 3);

      sequence.start(location, 1_000L);

      Assert.assertTrue(sequence.prepareSecondClick(location, 1_400L));
      Assert.assertTrue(sequence.completeSecondClick(location));
      Assert.assertFalse(sequence.isActive());
   }

   @Test
   public void differentCellDoesNotCompleteDoubleClick() {
      SelectionClickSequence sequence = new SelectionClickSequence(500L);
      sequence.start(new Point(4, 3), 1_000L);

      boolean prepared = sequence.prepareSecondClick(new Point(5, 3), 1_100L);

      Assert.assertFalse(prepared);
      Assert.assertFalse(sequence.isActive());
      Assert.assertFalse(sequence.completeSecondClick(new Point(5, 3)));
   }

   @Test
   public void clickAfterIntervalDoesNotCompleteDoubleClick() {
      SelectionClickSequence sequence = new SelectionClickSequence(500L);
      Point location = new Point(4, 3);
      sequence.start(location, 1_000L);

      boolean prepared = sequence.prepareSecondClick(location, 1_501L);

      Assert.assertFalse(prepared);
      Assert.assertFalse(sequence.isActive());
   }

   @Test
   public void secondClickReleasedOnDifferentCellDoesNotSelect() {
      SelectionClickSequence sequence = new SelectionClickSequence(500L);
      Point location = new Point(4, 3);
      sequence.start(location, 1_000L);

      Assert.assertTrue(sequence.prepareSecondClick(location, 1_100L));
      Assert.assertFalse(sequence.completeSecondClick(new Point(5, 3)));
      Assert.assertFalse(sequence.isActive());
   }
}
