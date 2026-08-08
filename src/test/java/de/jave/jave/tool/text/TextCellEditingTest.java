package de.jave.jave.tool.text;

import de.jave.lib.CharacterPlate;
import java.awt.Point;
import org.junit.Assert;
import org.junit.Test;

public class TextCellEditingTest {
   @Test
   public void combiningMarkAssimilatesIntoPreviousCellWithoutAdvancing() {
      CharacterPlate content = new CharacterPlate("a  ");
      Point cursor = new Point(1, 0);

      boolean consumedCell = TextCellEditing.enterGrapheme(content, cursor, "\u20D7", false);

      Assert.assertFalse(consumedCell);
      Assert.assertEquals("a\u20D7", content.textAt(0, 0));
      Assert.assertEquals(' ', content.glyphAt(1, 0));
   }

   @Test
   public void emojiModifierAndRegionalIndicatorAssimilateByGraphemeBoundary() {
      CharacterPlate emoji = new CharacterPlate("\uD83D\uDC4D  ");
      CharacterPlate flag = new CharacterPlate("\uD83C\uDDFA  ");

      Assert.assertFalse(TextCellEditing.enterGrapheme(emoji, new Point(1, 0), "\uD83C\uDFFB", false));
      Assert.assertFalse(TextCellEditing.enterGrapheme(flag, new Point(1, 0), "\uD83C\uDDF8", false));

      Assert.assertEquals("\uD83D\uDC4D\uD83C\uDFFB", emoji.textAt(0, 0));
      Assert.assertEquals("\uD83C\uDDFA\uD83C\uDDF8", flag.textAt(0, 0));
   }

   @Test
   public void insertModeShiftsAClusterAsOneCell() {
      CharacterPlate content = new CharacterPlate("Ae\u20DDB");

      Assert.assertTrue(TextCellEditing.enterGrapheme(content, new Point(1, 0), "X", true));

      Assert.assertEquals("AXe\u20DDB", content.asString());
      Assert.assertEquals(4, content.getWidth());
      Assert.assertTrue(content.cellAt(2, 0).isCluster());
   }

   @Test
   public void deleteShiftsAClusterAsOneCell() {
      CharacterPlate content = new CharacterPlate("Ae\u20DDB");

      TextRowEditing.deleteRightward(content, 0, 0);

      Assert.assertEquals("e\u20DDB ", content.getLine(0));
      Assert.assertTrue(content.cellAt(0, 0).isCluster());
   }

   @Test
   public void insertAtRightEdgeKeepsATrailingCursorCell() {
      CharacterPlate content = new CharacterPlate("A ");

      Assert.assertTrue(TextCellEditing.enterGrapheme(content, new Point(1, 0), "X", true));

      Assert.assertEquals("AX ", content.getLine(0));
      Assert.assertEquals(3, content.getWidth());
   }
}
