package de.jave.jave;

import java.awt.Point;
import org.junit.Assert;
import org.junit.Test;

public class SelectionTest {
   @Test
   public void restoringZeroHeightSelectionClearsSelection() {
      Selection selection = new Selection();

      selection.set(new Point(2, 3), new char[0][0], null);

      Assert.assertFalse(selection.hasSelection());
      Assert.assertNull(selection.getContent());
      Assert.assertNull(selection.getRegion());
   }

   @Test
   public void restoringZeroWidthSelectionClearsSelection() {
      Selection selection = new Selection();

      selection.set(new Point(2, 3), new char[1][0], null);

      Assert.assertFalse(selection.hasSelection());
      Assert.assertNull(selection.getContent());
      Assert.assertNull(selection.getRegion());
   }
}
