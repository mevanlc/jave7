package de.jave.jave;

import java.awt.Color;
import org.junit.Assert;
import org.junit.Test;

public class BoxDrawingPickerDialogTest {
   @Test
   public void selectionHintColorIsLighterAndPreservesAlpha() {
      Color selection = new Color(0, 96, 192, 128);

      Assert.assertEquals(
         new Color(170, 202, 234, 128),
         BoxDrawingPickerDialog.createSelectionHintColor(selection)
      );
   }

   @Test
   public void releaseMustRemainInsideThePressedGlyphCell() {
      Assert.assertTrue(BoxDrawingPickerDialog.isReleaseInPressedCell(2, 3, 2, 3));
      Assert.assertFalse(BoxDrawingPickerDialog.isReleaseInPressedCell(2, 3, 2, 4));
      Assert.assertFalse(BoxDrawingPickerDialog.isReleaseInPressedCell(2, 3, 3, 3));
      Assert.assertFalse(BoxDrawingPickerDialog.isReleaseInPressedCell(-1, -1, 2, 3));
   }

   @Test
   public void variantMenuFontIsFiftyPercentLarger() {
      Assert.assertEquals(21, BoxDrawingPickerDialog.variantMenuFontSize(20));
      Assert.assertEquals(24, BoxDrawingPickerDialog.variantMenuFontSize(24));
   }
}
