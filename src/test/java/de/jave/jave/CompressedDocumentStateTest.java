package de.jave.jave;

import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.layers.LayeredDocument;
import de.jave.jave.layers.SecondaryLayer;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
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

   @Test
   public void layeredSnapshotIsDetachedAndRestoresHiddenUnicodeContentAndMetadata() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abcde", "fghij"}));
      document.getDocumentLayer().setName("Background");
      SecondaryLayer layer = document.addSecondaryLayerAboveActive();
      layer.replaceContent(new Point(1, 1), new CharacterPlate("e\u20DD\uD83D\uDE00"));
      layer.setName("Ink: \uD83D\uDE00\nline two");
      layer.setVisible(false);
      layer.setOpaque(false);
      CompressedDocumentState state = layeredState(document);

      document.flatten(true);
      LayeredDocument restored = state.getLayeredDocument();

      Assert.assertEquals(2, restored.getLayerCount());
      Assert.assertEquals("Background", restored.getDocumentLayer().getName());
      Assert.assertArrayEquals(new String[]{"abcde", "fghij"}, restored.getDocumentLayer().getContent().toStringArray());
      Assert.assertEquals(layer.getId(), restored.getActiveLayerId());
      Assert.assertEquals(layer.getName(), restored.getActiveSecondaryLayer().getName());
      Assert.assertEquals(new Point(1, 1), restored.getActiveSecondaryLayer().getPosition());
      Assert.assertEquals(layer.getContent(), restored.getActiveSecondaryLayer().getContent());
      Assert.assertFalse(restored.getActiveSecondaryLayer().isVisible());
      Assert.assertFalse(restored.getActiveSecondaryLayer().isOpaque());
      Assert.assertArrayEquals(new String[]{"abcde", "fghij"}, new CharacterPlate(state.getContent()).toStringArray());
      restored.getActiveSecondaryLayer().getContent().clear();
      Assert.assertEquals(layer.getContent(), state.getLayeredDocument().getActiveSecondaryLayer().getContent());
      Assert.assertTrue(state.getEstimatedMemorySize() > 0);
   }

   @Test
   public void snapshotPreservesNextLayerNumberAfterDeletingHighestLayer() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate("abc"));
      document.addSecondaryLayerAboveActive();
      document.addSecondaryLayerAboveActive();
      document.deleteActiveLayer();

      LayeredDocument restored = layeredState(document).getLayeredDocument();

      Assert.assertEquals("layer-4", restored.addSecondaryLayerAboveActive().getId());
   }

   @Test
   public void restoringFlatStateReplacesAnExistingLayerStack() {
      PlateDocument document = PlateDocument.createNew(new Dimension(3, 1), ColorScheme.BLACK_ON_WHITE);
      document.addSecondaryLayerAboveActive();
      document.getEditableContent().set(1, 0, 'X');
      CompressedDocumentState state = new CompressedDocumentState();
      state.setContent("B3 1 abc");
      state.setColorScheme(ColorScheme.BLACK_ON_WHITE);

      document.setDocumentState(state);

      Assert.assertEquals(1, document.getLayerCount());
      Assert.assertTrue(document.isDocumentLayerActive());
      Assert.assertArrayEquals(new String[]{"abc"}, document.getContent().toStringArray());
   }

   private static CompressedDocumentState layeredState(LayeredDocument document) {
      return new CompressedDocumentState(document, new Point(), null, null, null, new Point(), null, "draw", ColorScheme.BLACK_ON_WHITE);
   }
}
