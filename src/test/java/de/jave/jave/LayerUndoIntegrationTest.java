package de.jave.jave;

import de.jave.jave.application.startup.ConfigurationList;
import de.jave.jave.menu.ProbePreferencesFactory;
import de.jave.lib.CharacterPlate;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class LayerUndoIntegrationTest {
   @BeforeClass
   public static void isolatePreferences() {
      System.setProperty("java.util.prefs.PreferencesFactory", ProbePreferencesFactory.class.getName());
   }

   @Test
   public void secondaryLayerDrawingCanBeUndoneAndRedoneWithoutChangingBaseLayer() throws Exception {
      withApplication(app -> {
         Plate plate = app.getMainPanel().getPlate();
         PlateDocument document = plate.getDocument();
         app.addSecondaryLayerAndActivate();
         plate.setChar(1, 0, 'X');
         plate.saveCurrentState("draw");

         app.doUndo();

         Assert.assertEquals(2, document.getLayerCount());
         Assert.assertEquals(' ', document.getEditableContent().glyphAt(1, 0));
         Assert.assertArrayEquals(new String[]{"abcde", "fghij"}, document.getContent().toStringArray());
         Assert.assertEquals(new Rectangle(0, 0, 0, 0), document.getActiveSecondaryLayer().getBounds());

         app.doRedo();

         Assert.assertEquals('X', document.getEditableContent().glyphAt(1, 0));
         Assert.assertArrayEquals(new String[]{"abcde", "fghij"}, document.getContent().toStringArray());
         Assert.assertArrayEquals(new String[]{"aXcde", "fghij"}, document.getCompositeContent().toStringArray());
      });
   }

   @Test
   public void creatingLayerIsAnUndoableOperation() throws Exception {
      withApplication(app -> {
         PlateDocument document = app.getMainPanel().getDocument();
         app.addSecondaryLayerAndActivate();

         app.doUndo();

         Assert.assertEquals(1, document.getLayerCount());
         Assert.assertTrue(document.isDocumentLayerActive());

         app.doRedo();

         Assert.assertEquals(2, document.getLayerCount());
         Assert.assertEquals(2, document.getActiveLayerNumber());
         Assert.assertArrayEquals(new String[]{"abcde", "fghij"}, document.getContent().toStringArray());
      });
   }

   @Test
   public void layerPropertiesCanBeUndoneEvenWhenCompositeDoesNotChange() throws Exception {
      withApplication(app -> {
         PlateDocument document = app.getMainPanel().getDocument();
         app.addSecondaryLayerAndActivate();
         String layerId = document.getActiveSecondaryLayer().getId();
         app.renameLayer(layerId, "Ink");
         app.setActiveLayerOpaque(false);
         app.toggleActiveLayerVisibility();

         app.doUndo();
         Assert.assertTrue(document.isActiveLayerVisible());
         Assert.assertFalse(document.isActiveLayerOpaque());
         app.doUndo();
         Assert.assertTrue(document.isActiveLayerOpaque());
         Assert.assertEquals("Ink", document.getActiveSecondaryLayer().getName());
         app.doUndo();
         Assert.assertEquals("Layer 2", document.getActiveSecondaryLayer().getName());

         app.doRedo();
         app.doRedo();
         app.doRedo();
         Assert.assertEquals("Ink", document.getActiveSecondaryLayer().getName());
         Assert.assertFalse(document.isActiveLayerOpaque());
         Assert.assertFalse(document.isActiveLayerVisible());
         Assert.assertArrayEquals(new String[]{"abcde", "fghij"}, document.getContent().toStringArray());
      });
   }

   @Test
   public void duplicateReorderDeleteAndFlattenRestoreIndependentLayers() throws Exception {
      withApplication(app -> {
         Plate plate = app.getMainPanel().getPlate();
         PlateDocument document = plate.getDocument();
         app.addSecondaryLayerAndActivate();
         plate.setChar(1, 0, 'X');
         plate.saveCurrentState("draw first");
         String firstId = document.getActiveSecondaryLayer().getId();
         app.duplicateActiveLayer();
         String copyId = document.getActiveSecondaryLayer().getId();
         plate.setChar(1, 0, 'Y');
         plate.saveCurrentState("draw copy");
         app.moveActiveLayerDown();
         Assert.assertArrayEquals(new String[]{"aXcde", "fghij"}, document.getCompositeContent().toStringArray());

         app.doUndo();
         Assert.assertEquals(firstId, document.getLayeredDocument().getSecondaryLayers().get(0).getId());
         Assert.assertEquals(copyId, document.getLayeredDocument().getSecondaryLayers().get(1).getId());
         Assert.assertArrayEquals(new String[]{"aYcde", "fghij"}, document.getCompositeContent().toStringArray());
         app.doRedo();
         app.deleteActiveLayer();
         Assert.assertEquals(2, document.getLayerCount());
         app.doUndo();
         Assert.assertEquals(3, document.getLayerCount());
         Assert.assertEquals(copyId, document.getActiveSecondaryLayer().getId());
         app.doRedo();
         app.doUndo();

         app.flattenLayers(true);
         Assert.assertEquals(1, document.getLayerCount());
         app.doUndo();
         Assert.assertEquals(3, document.getLayerCount());
         Assert.assertEquals(copyId, document.getActiveSecondaryLayer().getId());
         Assert.assertArrayEquals(new String[]{"abcde", "fghij"}, document.getContent().toStringArray());
         Assert.assertArrayEquals(new String[]{"aXcde", "fghij"}, document.getCompositeContent().toStringArray());
         app.doRedo();
         Assert.assertEquals(1, document.getLayerCount());
         Assert.assertArrayEquals(new String[]{"aXcde", "fghij"}, document.getContent().toStringArray());

         app.doUndo();
         app.doUndo();
         app.doUndo();
         Assert.assertEquals('X', document.getEditableContent().glyphAt(1, 0));
         app.doUndo();
         Assert.assertEquals(2, document.getLayerCount());
         Assert.assertEquals(firstId, document.getActiveSecondaryLayer().getId());
         app.doRedo();
         Assert.assertEquals(3, document.getLayerCount());
         Assert.assertEquals(copyId, document.getActiveSecondaryLayer().getId());
      });
   }

   @Test
   public void switchingLayersKeepsEditTargetAndPreservesRedo() throws Exception {
      withApplication(app -> {
         Plate plate = app.getMainPanel().getPlate();
         PlateDocument document = plate.getDocument();
         app.addSecondaryLayerAndActivate();
         plate.setChar(1, 0, 'X');
         plate.saveCurrentState("draw secondary");
         app.activateLayerNumber(1);
         plate.setChar(0, 0, 'A');
         plate.saveCurrentState("draw base");
         app.activateNextLayer();
         plate.setChar(2, 0, 'Y');
         plate.saveCurrentState("draw secondary again");

         app.doUndo();
         Assert.assertEquals(2, document.getActiveLayerNumber());
         Assert.assertEquals(' ', document.getEditableContent().glyphAt(2, 0));
         Assert.assertEquals('A', document.getContent().glyphAt(0, 0));
         app.activateNextLayer();
         Assert.assertTrue(plate.canRedo());
         Assert.assertEquals("draw base", plate.getUndoActionName());
         app.doRedo();
         Assert.assertEquals(2, document.getActiveLayerNumber());
         Assert.assertEquals('Y', document.getEditableContent().glyphAt(2, 0));

         app.doUndo();
         app.doUndo();
         Assert.assertEquals(1, document.getActiveLayerNumber());
         Assert.assertEquals('a', document.getContent().glyphAt(0, 0));
         Assert.assertEquals('X', document.getLayeredDocument().getSecondaryLayers().get(0).getGlyphAtDocument(1, 0));
      });
   }

   @Test
   public void secondarySelectionAndCursorSurviveUndoRedo() throws Exception {
      withApplication(app -> {
         Plate plate = app.getMainPanel().getPlate();
         PlateDocument document = plate.getDocument();
         app.addSecondaryLayerAndActivate();
         plate.setChar(1, 0, 'X');
         plate.setChar(3, 0, 'Y');
         plate.saveCurrentState("draw");
         document.getCursorLocation().setLocation(2, 1);
         plate.rememberCursorForUndo();
         plate.setSelection(new Rectangle(0, 0, 5, 1));
         plate.saveCurrentState("select");

         app.doUndo();
         Assert.assertFalse(plate.hasSelection());
         Assert.assertEquals(new Point(2, 1), document.getCursorLocation());
         Assert.assertEquals(new Rectangle(1, 0, 3, 1), document.getActiveSecondaryLayer().getBounds());
         app.doRedo();
         Assert.assertTrue(plate.hasSelection());
         Assert.assertEquals('X', document.getSelection().getContent().glyphAt(1, 0));
         Assert.assertEquals('Y', document.getSelection().getContent().glyphAt(3, 0));
         Assert.assertFalse(document.getSelection().getMask().isSet(0, 0));
         Assert.assertTrue(document.getSelection().getMask().isSet(2, 0));
         Assert.assertArrayEquals(new String[]{"abcde", "fghij"}, document.getContent().toStringArray());
      });
   }

   private static void withApplication(Consumer<JavEApplication> test) throws Exception {
      SwingUtilities.invokeAndWait(() -> {
         JavEApplication app = null;
         try {
            var init = Class.forName("de.jave.jave.application.startup.JaveStartup").getDeclaredMethod("initConfigFiles");
            init.setAccessible(true);
            app = new JavEApplication((ConfigurationList)init.invoke(null));
            app.startupMenuBar();
            app.doNew();
            Plate plate = app.getMainPanel().getPlate();
            plate.setContent(new CharacterPlate(new String[]{"abcde", "fghij"}));
            plate.saveCurrentState("base drawing");
            test.accept(app);
         } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
         } finally {
            if (app != null) {
               app.dispose();
            }
         }
      });
   }
}
