package de.jave.jave;

import de.jave.jave.preferences.ColorScheme;
import de.jave.lib.CharacterPlate;
import java.awt.Point;
import org.junit.Assert;
import org.junit.Test;

public class CompressedDocumentStateTest {
   @Test
   public void constructorIgnoresZeroHeightSelection() {
      CompressedDocumentState state = new CompressedDocumentState(
         new int[][]{{'a'}},
         new Point(0, 0),
         new int[0][0],
         new Point(2, 3),
         null,
         new Point(0, 0),
         null,
         "select",
         ColorScheme.BLACK_ON_WHITE
      );

      Assert.assertFalse(state.hasSelection());
      Assert.assertNull(state.getSelectionContent());
      Assert.assertNull(state.getSelectionLocation());
      Assert.assertNull(state.getSelectionMask());
      Assert.assertFalse(state.toString().contains("\nS:"));
   }

   @Test
   public void parsedZeroHeightSelectionIsNotSelection() {
      CompressedDocumentState state = new CompressedDocumentState();

      state.setSelectionContent("B0 0");
      state.setSelectionLocation(2, 3);

      Assert.assertFalse(state.hasSelection());
      Assert.assertNull(state.getSelectionContent());
      Assert.assertNull(state.getSelectionLocation());
   }

   @Test
   public void parsedAllSpaceSelectionKeepsSelectionDimensions() {
      CompressedDocumentState state = new CompressedDocumentState();

      state.setSelectionContent("B5 5");
      state.setSelectionLocation(2, 3);

      Assert.assertTrue(state.hasSelection());
      Assert.assertEquals(new Point(2, 3), state.getSelectionLocation());
      Assert.assertEquals(5, state.getSelectionContent().length);
      Assert.assertEquals(5, state.getSelectionContent()[0].length);
   }

   @Test
   public void clustersRoundTripThroughUndoStatePacking() {
      CharacterPlate content = new CharacterPlate("Ae\u20DD\uD83D\uDE00");
      CompressedDocumentState state = new CompressedDocumentState(
         content.glyphPlane(),
         new Point(0, 0),
         null,
         null,
         null,
         new Point(2, 0),
         "text",
         "type",
         ColorScheme.BLACK_ON_WHITE
      );

      Assert.assertTrue(state.getPackedContent().startsWith("C"));
      Assert.assertEquals(content, new CharacterPlate(state.getContent()));
   }
}
