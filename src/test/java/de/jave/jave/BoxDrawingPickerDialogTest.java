package de.jave.jave;

import de.jave.lib.CharacterPlate;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JLabel;
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
   public void glyphNameFontShrinksOnlyWhenNeededToFit() {
      JLabel label = new JLabel();
      Font preferredFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);
      String name = "LIGHT DIAGONAL UPPER CENTRE TO LOWER RIGHT";
      int preferredWidth = label.getFontMetrics(preferredFont).stringWidth(name);
      int constrainedWidth = label.getFontMetrics(preferredFont.deriveFont(8.0f)).stringWidth(name);

      Font wideFont = BoxDrawingPickerDialog.fitFontToWidth(
         label, preferredFont, 6.0f, name, preferredWidth
      );
      Font fittedFont = BoxDrawingPickerDialog.fitFontToWidth(
         label, preferredFont, 6.0f, name, constrainedWidth
      );

      Assert.assertEquals(preferredFont, wideFont);
      Assert.assertTrue(fittedFont.getSize2D() < preferredFont.getSize2D());
      Assert.assertTrue(label.getFontMetrics(fittedFont).stringWidth(name) <= constrainedWidth);
   }

   @Test
   public void insertAllLayoutMatchesThePickerGrid() {
      String[] rows = BoxDrawingPickerDialog.createPaletteLayoutRows();
      Set<Character> insertedCharacters = new HashSet<>();

      Assert.assertArrayEquals(new String[]{
         "┌─┬─┐ ┏━┳━┓ ╔═╦═╗",
         "│ │ │ ┃ ┃ ┃ ║ ║ ║",
         "├─┼─┤ ┣━╋━┫ ╠═╬═╣",
         "│ │ │ ┃ ┃ ┃ ║ ║ ║",
         "└─┴─┘ ┗━┻━┛ ╚═╩═╝",
         " ╱ ╲ ┈┈┈ ┉┉┉ ╭─╮",
         "  ╳  ┄┄┄ ┅┅┅ │ │",
         " ╲ ╱ ╌╌╌ ╍╍╍ ╰─╯",
         "     ┊┆╎ ┋┇╏",
         "     ┊┆╎ ┋┇╏"
      }, rows);
      for (String row : rows) {
         for (int i = 0; i < row.length(); i++) {
            if (row.charAt(i) != ' ') {
               insertedCharacters.add(row.charAt(i));
            }
         }
      }
      Assert.assertEquals(BoxDrawingPalette.getVisibleCharacters(), insertedCharacters);
   }

   @Test
   public void insertAllWritesOnlyPaletteGlyphs() {
      CharacterPlate target = new CharacterPlate(17, 10);
      target.setForce(5, 0, 'x');

      BoxDrawingPickerDialog.insertPaletteRows(
         target, new Point(0, 0), BoxDrawingPickerDialog.createPaletteLayoutRows()
      );

      Assert.assertEquals('x', target.get(5, 0));
      Assert.assertEquals('┌', target.get(0, 0));
      Assert.assertEquals('╏', target.get(11, 9));
   }
}
