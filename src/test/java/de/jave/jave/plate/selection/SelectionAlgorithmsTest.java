package de.jave.jave.plate.selection;

import de.jave.lib.CharacterPlate;
import java.awt.Point;
import java.awt.Rectangle;
import org.junit.Assert;
import org.junit.Test;

public class SelectionAlgorithmsTest {
   @Test
   public void connectedRegionUsesOnlyOrthogonallyConnectedNonSpaceCells() {
      CharacterPlate content = new CharacterPlate(new String[]{" AA  B", " A   B", "   A B"});

      Rectangle region = SelectionAlgorithms.findConnectedRegion(content, new Point(1, 0));

      Assert.assertEquals(new Rectangle(1, 0, 2, 2), region);
   }

   @Test
   public void connectedRegionIncludesItsBoundingRectangle() {
      CharacterPlate content = new CharacterPlate(new String[]{" XX", " X ", "XXX"});

      Rectangle region = SelectionAlgorithms.findConnectedRegion(content, new Point(1, 0));

      Assert.assertEquals(new Rectangle(0, 0, 3, 3), region);
   }

   @Test
   public void blankCellHasNoConnectedRegion() {
      CharacterPlate content = new CharacterPlate(new String[]{"A A"});

      Rectangle region = SelectionAlgorithms.findConnectedRegion(content, new Point(1, 0));

      Assert.assertNull(region);
   }

   @Test
   public void cellOutsideContentHasNoConnectedRegion() {
      CharacterPlate content = new CharacterPlate(new String[]{"A"});

      Rectangle region = SelectionAlgorithms.findConnectedRegion(content, new Point(2, 0));

      Assert.assertNull(region);
   }
}
