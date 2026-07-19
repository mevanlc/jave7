package de.jave.jave;

import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class BoxDrawingPaletteTest {
   @Test
   public void variantsCoverTheEntireUnicodeBoxDrawingBlockExactlyOnce() {
      Set<Character> seen = new HashSet<>();
      int count = 0;
      for (BoxDrawingPalette.Piece piece : BoxDrawingPalette.Piece.values()) {
         String variants = BoxDrawingPalette.getVariants(piece);
         for (int i = 0; i < variants.length(); i++) {
            char ch = variants.charAt(i);
            Assert.assertTrue("duplicate variant: " + ch, seen.add(ch));
            Assert.assertEquals(piece, BoxDrawingPalette.getPiece(ch));
            count++;
         }
      }

      Assert.assertEquals(128, count);
      for (int codePoint = BoxDrawingPalette.FIRST_CODE_POINT; codePoint <= BoxDrawingPalette.LAST_CODE_POINT; codePoint++) {
         Assert.assertTrue("missing U+" + Integer.toHexString(codePoint), seen.contains((char)codePoint));
      }
      Assert.assertEquals(seen, BoxDrawingPalette.getAllCharacters());
   }

   @Test
   public void transitionCharactersShareTheExpectedStructuralPosition() {
      Assert.assertEquals(BoxDrawingPalette.Piece.DOWN_HORIZONTAL, BoxDrawingPalette.getPiece('┭'));
      Assert.assertEquals(BoxDrawingPalette.Piece.VERTICAL_LEFT, BoxDrawingPalette.getPiece('┧'));
      Assert.assertEquals(BoxDrawingPalette.Piece.CROSS, BoxDrawingPalette.getPiece('╊'));
      Assert.assertEquals(BoxDrawingPalette.Piece.HORIZONTAL, BoxDrawingPalette.getPiece('╼'));
   }

   @Test
   public void alternateMenusExcludeCharactersAlreadyDrawnInTheDiagrams() {
      Set<Character> visible = BoxDrawingPalette.getVisibleCharacters();
      Set<Character> reachable = new HashSet<>(visible);
      for (char visibleCharacter : visible) {
         String alternates = BoxDrawingPalette.getAlternates(visibleCharacter);
         for (int i = 0; i < alternates.length(); i++) {
            Assert.assertFalse(visible.contains(alternates.charAt(i)));
            reachable.add(alternates.charAt(i));
         }
      }

      Assert.assertEquals(BoxDrawingPalette.getAllCharacters(), reachable);
      Assert.assertEquals("┍┎╒╓", BoxDrawingPalette.getAlternates('┌'));
      Assert.assertEquals("", BoxDrawingPalette.getAlternates('╳'));
   }

   @Test
   public void diagramsPresentTheCommonFamiliesAsDrawings() {
      Assert.assertTrue(diagramContains("Light", '┼'));
      Assert.assertTrue(diagramContains("Heavy", '╋'));
      Assert.assertTrue(diagramContains("Double", '╬'));
      Assert.assertTrue(diagramContains("Rounded", '╭'));
      Assert.assertTrue(diagramContains("Light dashes", '┄'));
      Assert.assertTrue(diagramContains("Heavy dashes", '╍'));
      Assert.assertTrue(diagramContains("Diagonals", '╳'));
   }

   private static boolean diagramContains(String title, char expected) {
      for (BoxDrawingPalette.Diagram diagram : BoxDrawingPalette.getDiagrams()) {
         if (title.equals(diagram.getTitle())) {
            for (String row : diagram.getRows()) {
               if (row.indexOf(expected) >= 0) {
                  return true;
               }
            }
         }
      }
      return false;
   }
}
