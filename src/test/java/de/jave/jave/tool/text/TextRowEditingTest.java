package de.jave.jave.tool.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.jave.jave.layers.ActiveLayerCharacterPlate;
import de.jave.jave.layers.LayeredDocument;
import de.jave.lib.CharacterPlate;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import org.junit.Test;

public class TextRowEditingTest {
   @Test
   public void controlSpaceInsertsSpaceAndGrowsDocumentToPreserveRightEdge() {
      CharacterPlate content = new CharacterPlate(new String[]{"abcd", "wxyz"});
      Point cursor = new Point(1, 0);

      boolean handled = TextRowEditing.handleShortcut(content, cursor, KeyEvent.VK_SPACE, InputEvent.CTRL_DOWN_MASK);

      assertTrue(handled);
      assertEquals(5, content.getWidth());
      assertEquals("a bcd", content.getLine(0));
      assertEquals("wxyz ", content.getLine(1));
      assertEquals(new Point(1, 0), cursor);
   }

   @Test
   public void controlSpaceUsesExistingTrailingSpaceWithoutGrowingDocument() {
      CharacterPlate content = new CharacterPlate("abcd ");

      boolean handled = TextRowEditing.handleShortcut(content, new Point(1, 0), KeyEvent.VK_SPACE, InputEvent.CTRL_DOWN_MASK);

      assertTrue(handled);
      assertEquals(5, content.getWidth());
      assertEquals("a bcd", content.getLine(0));
   }

   @Test
   public void controlShiftSpaceDeletesCharacterAndShiftsRemainderLeft() {
      CharacterPlate content = new CharacterPlate("abcde");

      boolean handled = TextRowEditing.handleShortcut(
         content,
         new Point(1, 0),
         KeyEvent.VK_SPACE,
         InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK
      );

      assertTrue(handled);
      assertEquals(5, content.getWidth());
      assertEquals("acde ", content.getLine(0));
   }

   @Test
   public void altRightAndLeftAliasRightwardInsertAndDelete() {
      CharacterPlate inserted = new CharacterPlate("abcd");
      CharacterPlate deleted = new CharacterPlate("abcde");

      assertTrue(
         TextRowEditing.handleShortcut(inserted, new Point(1, 0), KeyEvent.VK_RIGHT, InputEvent.ALT_DOWN_MASK)
      );
      assertTrue(
         TextRowEditing.handleShortcut(deleted, new Point(1, 0), KeyEvent.VK_LEFT, InputEvent.ALT_DOWN_MASK)
      );

      assertEquals("a bcd", inserted.getLine(0));
      assertEquals("acde ", deleted.getLine(0));
   }

   @Test
   public void altShiftLeftInsertsAtCursorAndShiftsLeftwardExtentLeft() {
      CharacterPlate content = new CharacterPlate(new String[]{"abcde", "vwxyz"});
      Point cursor = new Point(2, 0);

      boolean handled = TextRowEditing.handleShortcut(
         content,
         cursor,
         KeyEvent.VK_LEFT,
         InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK
      );

      assertTrue(handled);
      assertEquals(6, content.getWidth());
      assertEquals("abc de", content.getLine(0));
      assertEquals(" vwxyz", content.getLine(1));
      assertEquals(new Point(3, 0), cursor);
   }

   @Test
   public void altShiftLeftUsesExistingLeadingSpaceWithoutGrowingDocument() {
      CharacterPlate content = new CharacterPlate(" abcde");
      Point cursor = new Point(3, 0);

      boolean handled = TextRowEditing.handleShortcut(
         content,
         cursor,
         KeyEvent.VK_LEFT,
         InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK
      );

      assertTrue(handled);
      assertEquals(6, content.getWidth());
      assertEquals("abc de", content.getLine(0));
      assertEquals(new Point(3, 0), cursor);
   }

   @Test
   public void altShiftRightDeletesAtCursorAndShiftsLeftwardExtentRight() {
      CharacterPlate content = new CharacterPlate("abcde");
      Point cursor = new Point(2, 0);

      boolean handled = TextRowEditing.handleShortcut(
         content,
         cursor,
         KeyEvent.VK_RIGHT,
         InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK
      );

      assertTrue(handled);
      assertEquals(" abde", content.getLine(0));
      assertEquals(new Point(2, 0), cursor);
   }

   @Test
   public void leftwardGrowthWorksThroughActiveLayerProjection() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate("abcde"));
      CharacterPlate content = new ActiveLayerCharacterPlate(document);
      Point cursor = new Point(2, 0);

      assertTrue(
         TextRowEditing.handleShortcut(
            content,
            cursor,
            KeyEvent.VK_LEFT,
            InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK
         )
      );

      assertEquals(6, document.getSize().width);
      assertEquals("abc de", document.getActiveContentProjection().getLine(0));
      assertEquals(new Point(3, 0), cursor);
   }

   @Test
   public void altUpInsertsAtCursorAndShiftsUpwardExtentUp() {
      CharacterPlate content = new CharacterPlate(new String[]{"abc", "def", "ghi"});
      Point cursor = new Point(1, 1);

      boolean handled = TextRowEditing.handleShortcut(content, cursor, KeyEvent.VK_UP, InputEvent.ALT_DOWN_MASK);

      assertTrue(handled);
      assertEquals(4, content.getHeight());
      assertEquals(" b ", content.getLine(0));
      assertEquals("aec", content.getLine(1));
      assertEquals("d f", content.getLine(2));
      assertEquals("ghi", content.getLine(3));
      assertEquals(new Point(1, 2), cursor);
   }

   @Test
   public void altDownDeletesAtCursorAndShiftsUpwardExtentDown() {
      CharacterPlate content = new CharacterPlate(new String[]{"abc", "def", "ghi"});
      Point cursor = new Point(1, 1);

      boolean handled = TextRowEditing.handleShortcut(content, cursor, KeyEvent.VK_DOWN, InputEvent.ALT_DOWN_MASK);

      assertTrue(handled);
      assertEquals("a c", content.getLine(0));
      assertEquals("dbf", content.getLine(1));
      assertEquals("ghi", content.getLine(2));
      assertEquals(new Point(1, 1), cursor);
   }

   @Test
   public void altShiftDownInsertsAtCursorAndShiftsDownwardExtentDown() {
      CharacterPlate content = new CharacterPlate(new String[]{"abc", "def", "ghi"});
      Point cursor = new Point(1, 1);

      boolean handled = TextRowEditing.handleShortcut(
         content,
         cursor,
         KeyEvent.VK_DOWN,
         InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK
      );

      assertTrue(handled);
      assertEquals(4, content.getHeight());
      assertEquals("abc", content.getLine(0));
      assertEquals("d f", content.getLine(1));
      assertEquals("gei", content.getLine(2));
      assertEquals(" h ", content.getLine(3));
      assertEquals(new Point(1, 1), cursor);
   }

   @Test
   public void altShiftUpDeletesAtCursorAndShiftsDownwardExtentUp() {
      CharacterPlate content = new CharacterPlate(new String[]{"abc", "def", "ghi"});
      Point cursor = new Point(1, 1);

      boolean handled = TextRowEditing.handleShortcut(
         content,
         cursor,
         KeyEvent.VK_UP,
         InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK
      );

      assertTrue(handled);
      assertEquals("abc", content.getLine(0));
      assertEquals("dhf", content.getLine(1));
      assertEquals("g i", content.getLine(2));
      assertEquals(new Point(1, 1), cursor);
   }

   @Test
   public void verticalGrowthWorksThroughActiveLayerProjection() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc", "def", "ghi"}));
      CharacterPlate content = new ActiveLayerCharacterPlate(document);
      Point cursor = new Point(1, 1);

      assertTrue(TextRowEditing.handleShortcut(content, cursor, KeyEvent.VK_UP, InputEvent.ALT_DOWN_MASK));

      assertEquals(4, document.getSize().height);
      assertEquals(" b ", document.getActiveContentProjection().getLine(0));
      assertEquals("aec", document.getActiveContentProjection().getLine(1));
      assertEquals("d f", document.getActiveContentProjection().getLine(2));
      assertEquals("ghi", document.getActiveContentProjection().getLine(3));
      assertEquals(new Point(1, 2), cursor);
   }

   @Test
   public void otherKeysAndUnmodifiedSpaceAreNotHandled() {
      CharacterPlate content = new CharacterPlate("abcd");

      assertFalse(TextRowEditing.handleShortcut(content, new Point(1, 0), KeyEvent.VK_SPACE, 0));
      assertFalse(TextRowEditing.handleShortcut(content, new Point(1, 0), KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
      assertEquals("abcd", content.getLine(0));
   }
}
