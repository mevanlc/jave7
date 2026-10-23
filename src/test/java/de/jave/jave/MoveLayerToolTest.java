package de.jave.jave;

import de.jave.jave.actions.ToolBar;
import de.jave.jave.application.startup.ConfigurationList;
import de.jave.jave.layers.LayeredDocument;
import de.jave.jave.menu.ProbePreferencesFactory;
import de.jave.lib.CharacterPlate;
import de.jave.undo.UndoState;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MoveLayerToolTest {
   @BeforeClass
   public static void isolatePreferences() {
      System.setProperty("java.util.prefs.PreferencesFactory", ProbePreferencesFactory.class.getName());
   }

   @Test
   public void dragPreviewsWithoutChangingDocumentThenCommitsOneUndoStep() throws Exception {
      withApplication(app -> {
         Plate plate = app.getMainPanel().getPlate();
         PlateDocument document = plate.getDocument();
         CharacterPlate background = document.getContent().getClone();
         UndoState before = document.getUndoManager().getCurrentState();

         mouse(plate, MouseEvent.MOUSE_PRESSED, 3.25, 2.25);
         mouse(plate, MouseEvent.MOUSE_DRAGGED, 4.25, 3.25);
         mouse(plate, MouseEvent.MOUSE_DRAGGED, 6.25, 4.25);

         Assert.assertEquals(new Point(2, 1), document.getActiveSecondaryLayer().getPosition());
         Assert.assertFalse(document.isModified());
         Assert.assertSame(before, document.getUndoManager().getCurrentState());
         LayeredDocument preview = current(app).getDocumentPreview(document);
         Assert.assertEquals(new Point(5, 3), preview.getActiveSecondaryLayer().getPosition());
         Assert.assertEquals('X', preview.getComposite(false).glyphAt(5, 3));
         Assert.assertEquals(new Point(2, 1), plate.getDocumentState("snapshot").getLayeredDocument().getActiveSecondaryLayer().getPosition());

         mouse(plate, MouseEvent.MOUSE_RELEASED, 6.25, 4.25);

         Assert.assertNull(current(app).getDocumentPreview(document));
         Assert.assertEquals(new Point(5, 3), document.getActiveSecondaryLayer().getPosition());
         Assert.assertTrue(document.isModified());
         Assert.assertEquals(background, document.getContent());
         Assert.assertEquals("move layer", plate.getUndoActionName());
         app.doUndo();
         Assert.assertSame(before, document.getUndoManager().getCurrentState());
         Assert.assertEquals(new Point(2, 1), document.getActiveSecondaryLayer().getPosition());
         app.doRedo();
         Assert.assertEquals(new Point(5, 3), document.getActiveSecondaryLayer().getPosition());
         Assert.assertEquals(background, document.getContent());
      });
   }

   @Test
   public void escapeAndNoOpDragsPreserveRedoAndModificationState() throws Exception {
      withApplication(app -> {
         Plate plate = app.getMainPanel().getPlate();
         PlateDocument document = plate.getDocument();
         key(plate, KeyEvent.VK_RIGHT, 0);
         app.doUndo();
         document.setModified(false);
         UndoState before = document.getUndoManager().getCurrentState();

         mouse(plate, MouseEvent.MOUSE_PRESSED, 3.5, 2.5);
         mouse(plate, MouseEvent.MOUSE_DRAGGED, 8.5, 4.5);
         key(plate, KeyEvent.VK_ESCAPE, 0);
         mouse(plate, MouseEvent.MOUSE_RELEASED, 8.5, 4.5);
         mouse(plate, MouseEvent.MOUSE_PRESSED, 3.5, 2.5);
         mouse(plate, MouseEvent.MOUSE_DRAGGED, 8.5, 4.5);
         mouse(plate, MouseEvent.MOUSE_RELEASED, 3.5, 2.5);

         Assert.assertNull(current(app).getDocumentPreview(document));
         Assert.assertSame(before, document.getUndoManager().getCurrentState());
         Assert.assertFalse(document.isModified());
         Assert.assertTrue(plate.canRedo());
         Assert.assertEquals(new Point(2, 1), document.getActiveSecondaryLayer().getPosition());
      });
   }

   @Test
   public void contentCanMoveCompletelyOutsideCanvasAndBackWithoutBeingClipped() throws Exception {
      withApplication(app -> {
         Plate plate = app.getMainPanel().getPlate();
         PlateDocument document = plate.getDocument();
         CharacterPlate content = document.getActiveSecondaryLayer().getContent().getClone();

         mouse(plate, MouseEvent.MOUSE_PRESSED, 3.5, 2.5);
         mouse(plate, MouseEvent.MOUSE_DRAGGED, -5.5, -4.5);
         mouse(plate, MouseEvent.MOUSE_RELEASED, -5.5, -4.5);

         Assert.assertEquals(new Point(-7, -6), document.getActiveSecondaryLayer().getPosition());
         Assert.assertEquals(content, document.getActiveSecondaryLayer().getContent());
         Assert.assertEquals(document.getContent(), document.getCompositeContent());

         mouse(plate, MouseEvent.MOUSE_PRESSED, 3.5, 2.5);
         mouse(plate, MouseEvent.MOUSE_RELEASED, 12.5, 9.5);

         Assert.assertEquals(new Point(2, 1), document.getActiveSecondaryLayer().getPosition());
         Assert.assertEquals(content, document.getActiveSecondaryLayer().getContent());
         Assert.assertEquals('X', document.getCompositeContent().glyphAt(2, 1));
      });
   }

   @Test
   public void arrowKeysNudgeWithShiftForTenCellsAndSupportUndo() throws Exception {
      withApplication(app -> {
         Plate plate = app.getMainPanel().getPlate();
         PlateDocument document = plate.getDocument();

         key(plate, KeyEvent.VK_LEFT, 0);
         key(plate, KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK);
         key(plate, KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK);

         Assert.assertEquals(new Point(1, 11), document.getActiveSecondaryLayer().getPosition());
         app.doUndo();
         Assert.assertEquals(new Point(1, 1), document.getActiveSecondaryLayer().getPosition());
         app.doUndo();
         Assert.assertEquals(new Point(2, 1), document.getActiveSecondaryLayer().getPosition());
         app.doRedo();
         app.doRedo();
         Assert.assertEquals(new Point(1, 11), document.getActiveSecondaryLayer().getPosition());
      });
   }

   @Test
   public void documentAndEmptyLayersStayFixedAndSecondaryClicksDoNotMoveLayers() throws Exception {
      withApplication(app -> {
         Plate plate = app.getMainPanel().getPlate();
         PlateDocument document = plate.getDocument();
         UndoState before = document.getUndoManager().getCurrentState();
         Point point = plate.getScreenPointFor(3.5, 2.5);
         plate.mousePressed(new MouseEvent(plate, MouseEvent.MOUSE_PRESSED, 0, InputEvent.BUTTON3_DOWN_MASK,
            point.x, point.y, 1, true, MouseEvent.BUTTON3));
         mouse(plate, MouseEvent.MOUSE_DRAGGED, 8.5, 4.5);
         mouse(plate, MouseEvent.MOUSE_RELEASED, 8.5, 4.5);
         Assert.assertSame(before, document.getUndoManager().getCurrentState());
         Assert.assertEquals(new Point(2, 1), document.getActiveSecondaryLayer().getPosition());

         app.activateLayerNumber(1);
         CharacterPlate background = document.getContent().getClone();
         mouse(plate, MouseEvent.MOUSE_PRESSED, 3.5, 2.5);
         mouse(plate, MouseEvent.MOUSE_RELEASED, 8.5, 4.5);
         key(plate, KeyEvent.VK_LEFT, 0);
         Assert.assertSame(before, document.getUndoManager().getCurrentState());
         Assert.assertEquals(background, document.getContent());

         app.addSecondaryLayerAndActivate();
         before = document.getUndoManager().getCurrentState();
         mouse(plate, MouseEvent.MOUSE_PRESSED, 3.5, 2.5);
         mouse(plate, MouseEvent.MOUSE_RELEASED, 8.5, 4.5);
         key(plate, KeyEvent.VK_RIGHT, 0);
         Assert.assertSame(before, document.getUndoManager().getCurrentState());
         Assert.assertEquals(new Rectangle(), document.getActiveSecondaryLayer().getBounds());
      });
   }

   @Test
   public void changingToolFocusDocumentOrActiveLayerCancelsPreview() throws Exception {
      withApplication(app -> {
         Plate plate = app.getMainPanel().getPlate();
         PlateDocument document = plate.getDocument();
         MoveLayerTool tool = current(app);
         UndoState before = document.getUndoManager().getCurrentState();

         begin(plate);
         app.setTool(ToolBar.PAN_TOOL_INDEX);
         Assert.assertNull(tool.getDocumentPreview(document));
         app.setTool(ToolBar.MOVE_LAYER_TOOL_INDEX);
         begin(plate);
         for (var listener : plate.getFocusListeners()) {
            listener.focusLost(new FocusEvent(plate, FocusEvent.FOCUS_LOST));
         }
         mouse(plate, MouseEvent.MOUSE_RELEASED, 8.5, 4.5);
         Assert.assertNull(tool.getDocumentPreview(document));

         begin(plate);
         app.activateLayerNumber(1);
         Assert.assertNull(tool.getDocumentPreview(document));
         mouse(plate, MouseEvent.MOUSE_RELEASED, 8.5, 4.5);
         app.activateLayerNumber(2);
         begin(plate);
         app.doNew();
         app.setCurrentDocument(0);
         Assert.assertNull(tool.getDocumentPreview(document));
         mouse(plate, MouseEvent.MOUSE_RELEASED, 8.5, 4.5);

         Assert.assertEquals(new Point(2, 1), document.getActiveSecondaryLayer().getPosition());
         Assert.assertSame(before, document.getUndoManager().getCurrentState());
      });
   }

   @Test
   public void undoDuringDragDoesNotCommitPreviewIntoRestoredDocument() throws Exception {
      withApplication(app -> {
         Plate plate = app.getMainPanel().getPlate();
         begin(plate);

         app.doUndo();
         mouse(plate, MouseEvent.MOUSE_RELEASED, 8.5, 4.5);

         Assert.assertEquals(new Rectangle(), plate.getDocument().getActiveSecondaryLayer().getBounds());
         Assert.assertTrue(plate.canRedo());
         app.doRedo();
         Assert.assertEquals(new Point(2, 1), plate.getDocument().getActiveSecondaryLayer().getPosition());
      });
   }

   @Test
   public void hiddenLayerCanMoveWithoutChangingVisibilityOrOpacity() throws Exception {
      withApplication(app -> {
         Plate plate = app.getMainPanel().getPlate();
         PlateDocument document = plate.getDocument();
         app.setActiveLayerOpaque(false);
         app.toggleActiveLayerVisibility();
         begin(plate);
         Assert.assertEquals(document.getContent(), current(app).getDocumentPreview(document).getComposite(false));
         mouse(plate, MouseEvent.MOUSE_RELEASED, 8.5, 4.5);

         Assert.assertEquals(new Point(7, 3), document.getActiveSecondaryLayer().getPosition());
         Assert.assertFalse(document.isActiveLayerVisible());
         Assert.assertFalse(document.isActiveLayerOpaque());
         app.doUndo();
         Assert.assertEquals(new Point(2, 1), document.getActiveSecondaryLayer().getPosition());
         Assert.assertFalse(document.isActiveLayerVisible());
         Assert.assertFalse(document.isActiveLayerOpaque());
      });
   }

   @Test
   public void enteringMoveToolSettlesFloatingSelectionBeforeMovingWholeLayer() throws Exception {
      withApplication(app -> {
         Plate plate = app.getMainPanel().getPlate();
         PlateDocument document = plate.getDocument();
         app.switchToTextTool();
         plate.setSelection(new Rectangle(1, 1, 5, 1));
         plate.saveCurrentState("select");
         Assert.assertTrue(plate.hasSelection());
         app.setTool(ToolBar.MOVE_LAYER_TOOL_INDEX);

         Assert.assertFalse(plate.hasSelection());
         key(plate, KeyEvent.VK_RIGHT, 0);

         Assert.assertEquals(new Point(3, 1), document.getActiveSecondaryLayer().getPosition());
         Assert.assertEquals('X', document.getActiveSecondaryLayer().getGlyphAtDocument(3, 1));
         app.doUndo();
         app.doUndo();
         Assert.assertTrue(plate.hasSelection());
         key(plate, KeyEvent.VK_RIGHT, 0);
         Assert.assertFalse(plate.hasSelection());
         Assert.assertEquals('X', document.getActiveSecondaryLayer().getGlyphAtDocument(3, 1));
      });
   }

   private static MoveLayerTool current(JavEApplication app) {
      return (MoveLayerTool)app.getMainPanel().getToolManager().getTool(ToolBar.MOVE_LAYER_TOOL_INDEX);
   }

   private static void begin(Plate plate) {
      mouse(plate, MouseEvent.MOUSE_PRESSED, 3.5, 2.5);
      mouse(plate, MouseEvent.MOUSE_DRAGGED, 8.5, 4.5);
   }

   private static void mouse(Plate plate, int type, double x, double y) {
      Point point = plate.getScreenPointFor(x, y);
      int modifiers = type == MouseEvent.MOUSE_RELEASED ? 0 : InputEvent.BUTTON1_DOWN_MASK;
      MouseEvent event = new MouseEvent(plate, type, System.currentTimeMillis(), modifiers, point.x, point.y, 1, false,
         type == MouseEvent.MOUSE_DRAGGED ? MouseEvent.NOBUTTON : MouseEvent.BUTTON1);
      switch (type) {
         case MouseEvent.MOUSE_PRESSED -> plate.mousePressed(event);
         case MouseEvent.MOUSE_DRAGGED -> plate.mouseDragged(event);
         case MouseEvent.MOUSE_RELEASED -> plate.mouseReleased(event);
         default -> throw new AssertionError(type);
      }
   }

   private static void key(Plate plate, int code, int modifiers) {
      plate.keyPressed(new KeyEvent(plate, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), modifiers, code, KeyEvent.CHAR_UNDEFINED));
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
            plate.setContent(new CharacterPlate(new String[]{"abcdefghijkl", "abcdefghijkl", "abcdefghijkl",
               "abcdefghijkl", "abcdefghijkl", "abcdefghijkl"}));
            plate.saveCurrentState("background");
            app.addSecondaryLayerAndActivate();
            plate.setChar(2, 1, 'X');
            plate.setChar(4, 1, 'Y');
            plate.saveCurrentState("ink");
            app.setTool(ToolBar.MOVE_LAYER_TOOL_INDEX);
            plate.getDocument().setModified(false);
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
