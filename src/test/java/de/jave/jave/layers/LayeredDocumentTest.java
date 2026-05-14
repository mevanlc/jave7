package de.jave.jave.layers;

import de.jave.lib.CharacterPlate;
import de.jave.lib.area.BooleanArea;
import java.io.File;
import java.awt.Rectangle;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class LayeredDocumentTest {
   @Rule
   public TemporaryFolder temporaryFolder = new TemporaryFolder();

   @Test
   public void newSecondaryLayerHasZeroBoundsAndDoesNotHideDocumentLayer() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc"}));

      SecondaryLayer layer = document.addSecondaryLayerAboveActive();

      Assert.assertEquals(new Rectangle(0, 0, 0, 0), layer.getBounds());
      Assert.assertArrayEquals(new String[]{"abc"}, document.getComposite(false).toStringArray());
   }

   @Test
   public void opaqueSecondaryLayerSpacesHideLowerContentInsideBounds() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abcde"}));
      SecondaryLayer layer = document.addSecondaryLayerAboveActive();

      document.setActiveChar(1, 0, 'X');
      document.setActiveChar(3, 0, 'Y');

      Assert.assertArrayEquals(new String[]{"aX Ye"}, document.getComposite(false).toStringArray());
      Assert.assertEquals(new Rectangle(1, 0, 3, 1), layer.getBounds());
   }

   @Test
   public void nonOpaqueSecondaryLayerSpacesAllowLowerContentInsideBounds() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abcde"}));
      SecondaryLayer layer = document.addSecondaryLayerAboveActive();
      layer.setOpaque(false);

      document.setActiveChar(1, 0, 'X');
      document.setActiveChar(3, 0, 'Y');

      Assert.assertArrayEquals(new String[]{"aXcYe"}, document.getComposite(false).toStringArray());
   }

   @Test
   public void documentLayerSelectionCoverageIsUnmasked() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abcde"}));

      Assert.assertNull(document.getActiveLayerCoverageMask(new Rectangle(0, 0, 5, 1)));
   }

   @Test
   public void opaqueSecondaryLayerSelectionCoverageIncludesInternalSpacesOnlyInsideBounds() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abcde"}));
      document.addSecondaryLayerAboveActive();
      document.setActiveChar(1, 0, 'X');
      document.setActiveChar(3, 0, 'Y');

      BooleanArea mask = document.getActiveLayerCoverageMask(new Rectangle(0, 0, 5, 1));

      Assert.assertFalse(mask.isSet(0, 0));
      Assert.assertTrue(mask.isSet(1, 0));
      Assert.assertTrue(mask.isSet(2, 0));
      Assert.assertTrue(mask.isSet(3, 0));
      Assert.assertFalse(mask.isSet(4, 0));
   }

   @Test
   public void nonOpaqueSecondaryLayerSelectionCoverageTreatsSpacesAsTransparent() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abcde"}));
      SecondaryLayer layer = document.addSecondaryLayerAboveActive();
      layer.setOpaque(false);
      document.setActiveChar(1, 0, 'X');
      document.setActiveChar(3, 0, 'Y');

      BooleanArea mask = document.getActiveLayerCoverageMask(new Rectangle(0, 0, 5, 1));

      Assert.assertFalse(mask.isSet(0, 0));
      Assert.assertTrue(mask.isSet(1, 0));
      Assert.assertFalse(mask.isSet(2, 0));
      Assert.assertTrue(mask.isSet(3, 0));
      Assert.assertFalse(mask.isSet(4, 0));
   }

   @Test
   public void drawingNonSpaceOutsideCurrentBoundsExpandsSecondaryLayer() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{".....", ".....", "....."}));
      SecondaryLayer layer = document.addSecondaryLayerAboveActive();

      document.setActiveChar(3, 2, 'Z');

      Assert.assertEquals(new Rectangle(3, 2, 1, 1), layer.getBounds());
      Assert.assertArrayEquals(new String[]{".....", ".....", "...Z."}, document.getComposite(false).toStringArray());
   }

   @Test
   public void drawingSpaceOutsideCurrentBoundsDoesNotExpandSecondaryLayer() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc"}));
      SecondaryLayer layer = document.addSecondaryLayerAboveActive();

      document.setActiveChar(1, 0, ' ');

      Assert.assertEquals(new Rectangle(0, 0, 0, 0), layer.getBounds());
      Assert.assertArrayEquals(new String[]{"abc"}, document.getComposite(false).toStringArray());
   }

   @Test
   public void activeLayerNumberTracksDocumentOrder() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc"}));

      Assert.assertEquals(1, document.getActiveLayerNumber());
      document.addSecondaryLayerAboveActive();

      Assert.assertEquals(2, document.getActiveLayerNumber());
   }

   @Test
   public void renameLayerTrimsAndUpdatesLayerName() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc"}));
      SecondaryLayer layer = document.addSecondaryLayerAboveActive();

      Assert.assertTrue(document.renameLayer(layer.getId(), "  Ink  "));

      Assert.assertEquals("Ink", layer.getName());
   }

   @Test
   public void duplicatingDocumentLayerCreatesSecondaryLayerCopy() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc"}));

      SecondaryLayer duplicate = document.duplicateActiveLayer();

      Assert.assertEquals(2, document.getLayerCount());
      Assert.assertEquals(2, document.getActiveLayerNumber());
      Assert.assertEquals(new Rectangle(0, 0, 3, 1), duplicate.getBounds());
      Assert.assertArrayEquals(new String[]{"abc"}, document.getComposite(false).toStringArray());
   }

   @Test
   public void duplicatingSecondaryLayerPreservesPositionAndContent() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abcde"}));
      SecondaryLayer original = document.addSecondaryLayerAboveActive();
      document.setActiveChar(3, 0, 'Z');

      SecondaryLayer duplicate = document.duplicateActiveLayer();

      Assert.assertEquals(3, document.getLayerCount());
      Assert.assertEquals(3, document.getActiveLayerNumber());
      Assert.assertEquals(original.getBounds(), duplicate.getBounds());
      Assert.assertArrayEquals(original.getContent().toStringArray(), duplicate.getContent().toStringArray());
   }

   @Test
   public void activeDocumentLayerCannotBeDeleted() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc"}));

      Assert.assertFalse(document.canDeleteActiveLayer());
      Assert.assertFalse(document.deleteActiveLayer());
      Assert.assertEquals(1, document.getLayerCount());
   }

   @Test
   public void activeSecondaryLayerCanBeDeleted() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc"}));
      document.addSecondaryLayerAboveActive();

      Assert.assertTrue(document.canDeleteActiveLayer());
      Assert.assertTrue(document.deleteActiveLayer());

      Assert.assertEquals(1, document.getLayerCount());
      Assert.assertEquals(1, document.getActiveLayerNumber());
   }

   @Test
   public void activeSecondaryLayerCanMoveDownAndChangeCompositeOrder() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"."}));
      SecondaryLayer lower = document.addSecondaryLayerAboveActive();
      document.setActiveChar(0, 0, 'A');
      SecondaryLayer upper = document.addSecondaryLayerAboveActive();
      document.setActiveChar(0, 0, 'B');

      Assert.assertTrue(document.canMoveActiveLayerDown());
      Assert.assertTrue(document.moveActiveLayerDown());

      Assert.assertEquals(2, document.getActiveLayerNumber());
      Assert.assertSame(upper, document.getSecondaryLayers().get(0));
      Assert.assertSame(lower, document.getSecondaryLayers().get(1));
      Assert.assertArrayEquals(new String[]{"A"}, document.getComposite(false).toStringArray());
   }

   @Test
   public void activeSecondaryLayerCanMoveUpAndChangeCompositeOrder() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"."}));
      SecondaryLayer lower = document.addSecondaryLayerAboveActive();
      document.setActiveChar(0, 0, 'A');
      SecondaryLayer upper = document.addSecondaryLayerAboveActive();
      document.setActiveChar(0, 0, 'B');
      document.activateLayerNumber(2);

      Assert.assertTrue(document.canMoveActiveLayerUp());
      Assert.assertTrue(document.moveActiveLayerUp());

      Assert.assertEquals(3, document.getActiveLayerNumber());
      Assert.assertSame(upper, document.getSecondaryLayers().get(0));
      Assert.assertSame(lower, document.getSecondaryLayers().get(1));
      Assert.assertArrayEquals(new String[]{"A"}, document.getComposite(false).toStringArray());
   }

   @Test
   public void documentLayerAndBottomSecondaryLayerCannotMoveDown() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc"}));

      Assert.assertFalse(document.canMoveActiveLayerDown());
      Assert.assertFalse(document.moveActiveLayerDown());

      document.addSecondaryLayerAboveActive();

      Assert.assertFalse(document.canMoveActiveLayerDown());
      Assert.assertFalse(document.moveActiveLayerDown());
   }

   @Test
   public void documentLayerVisibilityAndOpacityCannotBeToggled() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc"}));

      Assert.assertFalse(document.canToggleActiveLayerVisibility());
      Assert.assertFalse(document.toggleActiveLayerVisibility());
      Assert.assertTrue(document.isActiveLayerVisible());
      Assert.assertFalse(document.canToggleActiveLayerOpacity());
      Assert.assertFalse(document.setActiveLayerOpaque(false));
      Assert.assertTrue(document.isActiveLayerOpaque());
   }

   @Test
   public void activeSecondaryLayerVisibilityCanBeToggled() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc"}));
      document.addSecondaryLayerAboveActive();
      document.setActiveChar(1, 0, 'X');

      Assert.assertTrue(document.canToggleActiveLayerVisibility());
      Assert.assertTrue(document.isActiveLayerVisible());
      Assert.assertArrayEquals(new String[]{"aXc"}, document.getComposite(false).toStringArray());

      Assert.assertTrue(document.toggleActiveLayerVisibility());

      Assert.assertFalse(document.isActiveLayerVisible());
      Assert.assertArrayEquals(new String[]{"abc"}, document.getComposite(false).toStringArray());
      Assert.assertArrayEquals(new String[]{"aXc"}, document.getComposite(true).toStringArray());
   }

   @Test
   public void activeSecondaryLayerOpacityCanBeToggled() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abcde"}));
      document.addSecondaryLayerAboveActive();
      document.setActiveChar(1, 0, 'X');
      document.setActiveChar(3, 0, 'Y');

      Assert.assertTrue(document.canToggleActiveLayerOpacity());
      Assert.assertTrue(document.isActiveLayerOpaque());
      Assert.assertArrayEquals(new String[]{"aX Ye"}, document.getComposite(false).toStringArray());

      Assert.assertTrue(document.setActiveLayerOpaque(false));

      Assert.assertFalse(document.isActiveLayerOpaque());
      Assert.assertArrayEquals(new String[]{"aXcYe"}, document.getComposite(false).toStringArray());
   }

   @Test
   public void activatingNextLayerCyclesThroughDocumentOrder() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc"}));
      document.addSecondaryLayerAboveActive();
      document.addSecondaryLayerAboveActive();

      Assert.assertEquals(3, document.getActiveLayerNumber());
      document.activateNextLayer();
      Assert.assertEquals(1, document.getActiveLayerNumber());
      document.activateNextLayer();
      Assert.assertEquals(2, document.getActiveLayerNumber());
      document.activateNextLayer();
      Assert.assertEquals(3, document.getActiveLayerNumber());
   }

   @Test
   public void hiddenSecondaryLayerIsIncludedOnlyWhenRequested() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc"}));
      SecondaryLayer layer = document.addSecondaryLayerAboveActive();
      document.setActiveChar(1, 0, 'X');
      layer.setVisible(false);

      Assert.assertArrayEquals(new String[]{"abc"}, document.getComposite(false).toStringArray());
      Assert.assertArrayEquals(new String[]{"aXc"}, document.getComposite(true).toStringArray());
   }

   @Test
   public void activeLayerAdapterBulkEditsSecondaryLayer() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc", "def"}));
      document.addSecondaryLayerAboveActive();
      ActiveLayerCharacterPlate activeLayer = new ActiveLayerCharacterPlate(document);

      activeLayer.paste("XY", 1, 0);
      activeLayer.insertLine(1, "Z");

      Assert.assertEquals(3, document.getSize().height);
      Assert.assertArrayEquals(new String[]{" XY", "Z", ""}, document.getComposite(false).toStringArray());
      Assert.assertArrayEquals(new String[]{" XY", "Z", ""}, document.getActiveContentProjection().toStringArray());
   }

   @Test
   public void resizingDocumentClipsSecondaryLayersWithoutMovingThem() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abcde", "fghij"}));
      document.addSecondaryLayerAboveActive();
      document.setActiveChar(3, 1, 'Z');

      document.resizeDocument(3, 1);

      Assert.assertEquals(new Rectangle(3, 1, 0, 0), document.getActiveSecondaryLayer().getBounds());
      Assert.assertArrayEquals(new String[]{"abc"}, document.getComposite(false).toStringArray());
   }

   @Test
   public void flattenVisibleDropsHiddenSecondaryLayers() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc"}));
      SecondaryLayer layer = document.addSecondaryLayerAboveActive();
      document.setActiveChar(1, 0, 'X');
      layer.setVisible(false);

      document.flatten(false);

      Assert.assertEquals(1, document.getLayerCount());
      Assert.assertArrayEquals(new String[]{"abc"}, document.getDocumentLayer().getContent().toStringArray());
   }

   @Test
   public void flattenIncludesHiddenSecondaryLayersWhenRequested() {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abc"}));
      SecondaryLayer layer = document.addSecondaryLayerAboveActive();
      document.setActiveChar(1, 0, 'X');
      layer.setVisible(false);

      document.flatten(true);

      Assert.assertEquals(1, document.getLayerCount());
      Assert.assertArrayEquals(new String[]{"aXc"}, document.getDocumentLayer().getContent().toStringArray());
   }

   @Test
   public void javeDocArchiveRoundTripsLayerMetadataAndContent() throws Exception {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate(new String[]{"abcde", "fghij"}));
      SecondaryLayer first = document.addSecondaryLayerAboveActive();
      first.setName("Speech Bubble!");
      first.setOpaque(false);
      document.setActiveChar(3, 1, 'Z');
      SecondaryLayer second = document.addSecondaryLayerAboveActive();
      second.setName("Speech Bubble!");
      second.setVisible(false);
      document.setActiveChar(0, 0, 'Q');
      File archive = this.temporaryFolder.newFile("roundtrip.javedoc");

      JaveDocArchive.write(document, archive);
      LayeredDocument loaded = JaveDocArchive.read(archive);

      Assert.assertEquals(3, loaded.getLayerCount());
      Assert.assertEquals(document.getActiveLayerId(), loaded.getActiveLayerId());
      Assert.assertArrayEquals(document.getComposite(true).toStringArray(), loaded.getComposite(true).toStringArray());
      Assert.assertEquals("Speech Bubble!", loaded.getSecondaryLayers().get(0).getName());
      Assert.assertFalse(loaded.getSecondaryLayers().get(0).isOpaque());
      Assert.assertEquals(first.getPosition(), loaded.getSecondaryLayers().get(0).getPosition());
      Assert.assertFalse(loaded.getSecondaryLayers().get(1).isVisible());
   }
}
