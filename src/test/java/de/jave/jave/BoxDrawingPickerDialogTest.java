package de.jave.jave;

import de.jave.lib.CharacterPlate;
import java.awt.Color;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
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

   @Test
   public void insertAllLayoutMatchesThePickerGrid() {
      String[] rows = BoxDrawingPickerDialog.createPaletteLayoutRows();
      Set<Character> insertedCharacters = new HashSet<>();

      Assert.assertEquals(11, rows.length);
      for (String row : rows) {
         Assert.assertEquals(24, row.length());
         for (int i = 0; i < row.length(); i++) {
            if (row.charAt(i) != ' ') {
               insertedCharacters.add(row.charAt(i));
            }
         }
      }
      Assert.assertEquals('┌', rows[0].charAt(1));
      Assert.assertEquals('┏', rows[0].charAt(9));
      Assert.assertEquals('╔', rows[0].charAt(17));
      Assert.assertEquals('┄', rows[5].charAt(6));
      Assert.assertEquals('┅', rows[5].charAt(12));
      Assert.assertEquals('╱', rows[6].charAt(1));
      Assert.assertEquals('╭', rows[6].charAt(19));
      Assert.assertEquals(BoxDrawingPalette.getVisibleCharacters(), insertedCharacters);
   }

   @Test
   public void insertAllWritesOnlyPaletteGlyphs() {
      CharacterPlate target = new CharacterPlate(24, 11);
      target.setForce(0, 0, 'x');

      BoxDrawingPickerDialog.insertPaletteRows(
         target, new Point(0, 0), BoxDrawingPickerDialog.createPaletteLayoutRows()
      );

      Assert.assertEquals('x', target.get(0, 0));
      Assert.assertEquals('┌', target.get(1, 0));
      Assert.assertEquals('╏', target.get(16, 10));
   }
}
