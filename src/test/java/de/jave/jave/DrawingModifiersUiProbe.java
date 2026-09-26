package de.jave.jave;

import static java.awt.event.InputEvent.*;
import static java.awt.event.KeyEvent.*;
import static java.awt.event.MouseEvent.*;
import static de.jave.jave.actions.ToolBar.*;

import de.jave.jave.application.startup.ConfigurationList;
import de.jave.jave.filter.Filter;
import de.jave.jave.filter.FilterMatrix;
import de.jave.jave.figlet.FigletToolOptionsPanel;
import de.jave.jave.menu.ProbePreferencesFactory;
import de.jave.jave.pixelplate.PixelPlate;
import de.jave.jave.pixelplate.PixelPlateMode;
import de.jave.jave.tool.linealgorithmic.AlgorithmicLineStyle;
import de.jave.jave.tool.linealgorithmic.ArrowheadPlacement;
import de.jave.lib.CharacterPlate;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.dizzy.commons.swing.mousecursor.CursorId;
import net.dizzy.commons.swing.mousecursor.CursorProvider;

/** Run explicitly with drawingModifiersUiProbe; uses isolated preferences and documents. */
public final class DrawingModifiersUiProbe {
   private static JavEApplication app;
   private static Plate plate;
   private static JFrame frame;
   private static Robot robot;
   private static final Path OUTPUT = Path.of(System.getProperty("jave.probeOutput"));

   public static void main(String[] args) throws Exception {
      int status = 0;
      try {
         System.setProperty("java.util.prefs.PreferencesFactory", ProbePreferencesFactory.class.getName());
         edt(() -> {
            try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
               var init = Class.forName("de.jave.jave.application.startup.JaveStartup").getDeclaredMethod("initConfigFiles");
               init.setAccessible(true);
               app = new JavEApplication((ConfigurationList)init.invoke(null));
               app.startupMenuBar();
               app.doNew();
               plate = app.getMainPanel().getPlate();
               frame = app.getFrame();
               frame.setTitle("JavE drawing modifier probe");
               frame.setBounds(120, 100, 1100, 740);
               frame.setAlwaysOnTop(true);
               frame.setVisible(true);
               frame.toFront();
               Desktop.getDesktop().requestForeground(true);
               app.setTool(LINE_ALGORITHMIC_TOOL_INDEX);
               plate.requestFocusInWindow();
               return null;
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         });
         robot = new Robot();
         robot.setAutoDelay(70);
         await(() -> plate.isFocusOwner(), "canvas focused");
         edt(() -> { selectionCursorUndo(false); selectionCursorUndo(true); return null; });
         edt(() -> { routedEvents(); return null; });
         nativeInput();
         System.out.println("ALL DRAWING MODIFIER PROBE CHECKS PASSED");
      } catch (Throwable error) {
         error.printStackTrace();
         status = 1;
      } finally {
         if (robot != null) {
            robot.mouseRelease(BUTTON1_DOWN_MASK);
            robot.keyRelease(VK_CONTROL);
            robot.keyRelease(VK_ALT);
            robot.keyRelease(VK_SHIFT);
         }
         if (frame != null) edt(() -> { frame.dispose(); return null; });
      }
      System.exit(status);
   }

   private static void routedEvents() {
      arrowPlacement(ArrowheadPlacement.NONE);
      Point start = plate.getScreenPointFor(4.12, 4.18);
      Point end = plate.getScreenPointFor(24.88, 10.84);
      begin(start, end, 0);
      for (int modifiers : new int[]{0, CTRL_DOWN_MASK, CTRL_DOWN_MASK | ALT_DOWN_MASK, ALT_DOWN_MASK, 0,
                                    ALT_DOWN_MASK, CTRL_DOWN_MASK | ALT_DOWN_MASK, CTRL_DOWN_MASK}) {
         key(KEY_RELEASED, VK_F6, modifiers);
         check(preview().equals(expectedLine(start, end, modifiers)), "line geometry for modifiers " + modifiers);
      }
      key(KEY_PRESSED, VK_RIGHT, CTRL_DOWN_MASK | ALT_DOWN_MASK);
      start.translate(1, 0);
      key(KEY_RELEASED, VK_RIGHT, CTRL_DOWN_MASK | ALT_DOWN_MASK);
      check(Tool.controlDown && Tool.altDown, "arrow key release preserves both modifiers");
      check(preview().equals(expectedLine(start, end, CTRL_DOWN_MASK | ALT_DOWN_MASK)), "origin nudging while snapped");
      key(KEY_RELEASED, VK_CONTROL, ALT_DOWN_MASK);
      check(preview().equals(expectedLine(start, end, ALT_DOWN_MASK)), "origin restores nudged raw position");

      Point release = plate.getScreenPointFor(28.8, 12.2);
      String expected = expectedLine(start, release, CTRL_DOWN_MASK | ALT_DOWN_MASK);
      plate.getContent().clear();
      mouse(MOUSE_RELEASED, release, CTRL_DOWN_MASK | ALT_DOWN_MASK);
      check(expected.equals(plate.getContent().toString()), "commit uses release position and modifiers");
      check(Tool.controlDown && Tool.altDown && current().markPlate == null, "commit clears gesture and preserves held keys");
      begin(start, end, CTRL_DOWN_MASK | ALT_DOWN_MASK);
      check(preview().equals(expectedLine(start, end, CTRL_DOWN_MASK | ALT_DOWN_MASK)), "held modifiers work on next drag");
      key(KEY_PRESSED, VK_ESCAPE, CTRL_DOWN_MASK | ALT_DOWN_MASK);
      check(current().markPlate == null && Tool.controlDown && Tool.altDown, "Escape cancels only the gesture");

      begin(start, end, CTRL_DOWN_MASK | ALT_DOWN_MASK);
      for (var listener : plate.getFocusListeners()) listener.focusLost(new FocusEvent(plate, FocusEvent.FOCUS_LOST));
      check(!Tool.controlDown && !Tool.altDown, "focus loss clears stale modifiers");
      check(preview().equals(expectedLine(start, end, 0)), "focus loss removes snaps");
      mouse(MOUSE_DRAGGED, end, ALT_DOWN_MASK);
      check(preview().equals(expectedLine(start, end, ALT_DOWN_MASK)), "mouse event restores actual modifiers");
      app.setTool(RECTANGLE_ALGORITHMIC_TOOL_INDEX);
      app.setTool(LINE_ALGORITHMIC_TOOL_INDEX);
      check(Tool.altDown && current().markPlate == null, "tool changes preserve modifiers and discard old line");
      key(KEY_PRESSED, VK_PAGE_DOWN, CTRL_DOWN_MASK | SHIFT_DOWN_MASK);
      check(Tool.controlDown && Tool.shiftDown && !Tool.altDown, "canvas shortcut synchronizes modifiers before returning");

      selectionEvents();
      shapeEvents();
      textAndFigletEvents();
      app.setTool(LINE_ALGORITHMIC_TOOL_INDEX);
      for (int zoom = 0; zoom < 3; zoom++) {
         plate.getZoomableFontModel().zoomIn();
         Point zoomStart = plate.getScreenPointFor(4.12, 4.18);
         Point zoomEnd = plate.getScreenPointFor(24.88, 10.84);
         begin(zoomStart, zoomEnd, CTRL_DOWN_MASK | ALT_DOWN_MASK);
         check(preview().equals(expectedLine(zoomStart, zoomEnd, CTRL_DOWN_MASK | ALT_DOWN_MASK)),
            "snapping with cell size " + plate.getCharWidth() + "x" + plate.getCharHeight());
         key(KEY_PRESSED, VK_ESCAPE, CTRL_DOWN_MASK | ALT_DOWN_MASK);
      }
      for (int zoom = 0; zoom < 3; zoom++) plate.getZoomableFontModel().zoomOut();
      key(KEY_RELEASED, VK_SHIFT, 0);
      plate.getContent().clear();
      arrowPlacement(ArrowheadPlacement.BOTH);
   }

   private static void selectionCursorUndo(boolean clickWithSelectionTool) {
      app.switchToTextTool(1, 1);
      plate.setChar(0, 0, 'X');
      plate.saveCurrentState("fixture");
      if (clickWithSelectionTool) app.switchToSelectonTool();
      Point clicked = new Point(8, 6);
      Point start = plate.getScreenPointFor(8.5, 6.5);
      mouse(MOUSE_PRESSED, start, 0);
      mouse(MOUSE_RELEASED, start, 0);
      check(plate.getDocument().getCursorLocation().equals(clicked), "click moves cursor");
      check(plate.getUndoActionName().equals("fixture"), "click adds no undo step");
      app.selectAll();
      plate.undo();
      check(!plate.hasSelection() && plate.getDocument().getCursorLocation().equals(clicked), "undo select-all restores clicked cursor");
      plate.redo();
      check(plate.hasSelection(), "redo restores selection");
      plate.undo();
      app.switchToTextTool();
      Point redoClick = plate.getScreenPointFor(9.5, 6.5);
      mouse(MOUSE_PRESSED, redoClick, 0);
      mouse(MOUSE_RELEASED, redoClick, 0);
      check(plate.canRedo(), "cursor click preserves redo");
      Point end = plate.getScreenPointFor(12.5, 9.5);
      mouse(MOUSE_PRESSED, start, 0);
      mouse(MOUSE_DRAGGED, end, 0);
      mouse(MOUSE_RELEASED, end, 0);
      check(plate.hasSelection() && plate.getUndoActionName().equals("select"), "text-tool drag records a selection undo step");
      plate.undo();
      check(!plate.hasSelection() && plate.getDocument().getCursorLocation().equals(clicked), "undo text drag restores clicked cursor");
      check(plate.getChar(0, 0) == 'X', "undo selection preserves preceding edit");
      plate.redo();
      check(plate.hasSelection(), "redo text drag restores selection");
      plate.undo();
      app.switchToSelectonTool();
      Point other = plate.getScreenPointFor(16.5, 6.5);
      mouse(MOUSE_PRESSED, other, 0);
      mouse(MOUSE_DRAGGED, end, 0);
      mouse(MOUSE_RELEASED, end, 0);
      plate.undo();
      check(!plate.hasSelection() && plate.getDocument().getCursorLocation().equals(clicked), "undo selection-tool drag preserves prior clicked cursor");
      plate.undo();
      check(plate.getChar(0, 0) == ' ', "next undo reaches preceding edit without cursor-only steps");
      app.setTool(LINE_ALGORITHMIC_TOOL_INDEX);
   }

   private static void selectionEvents() {
      app.setTool(SELECTION_TOOL_INDEX);
      plate.setSelection(new Rectangle(5, 5, 3, 3));
      SelectionTool tool = (SelectionTool)current();
      Point start = plate.getScreenPointFor(12.2, 6.2);
      Point end = plate.getScreenPointFor(16.2, 8.2);
      begin(start, end, SHIFT_DOWN_MASK | CTRL_DOWN_MASK);
      check(tool.mode == SelectionTool.SELECT_PLUS, "Shift takes precedence for selection");
      key(KEY_RELEASED, VK_F6, SHIFT_DOWN_MASK | CTRL_DOWN_MASK);
      key(KEY_RELEASED, VK_CONTROL, SHIFT_DOWN_MASK);
      check(tool.mode == SelectionTool.SELECT_PLUS, "unrelated releases preserve add-selection gesture");
      check(tool.getCursor() == CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION_PLUS), "add-selection cursor stays active");
      key(KEY_RELEASED, VK_SHIFT, CTRL_DOWN_MASK);
      check(tool.mode == SelectionTool.NONE && tool.location1 == null, "Shift release cancels add-selection gesture");
      check(tool.getCursor() == CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION_MINUS), "remaining Ctrl selects subtract cursor");
      begin(start, end, CTRL_DOWN_MASK);
      key(KEY_RELEASED, VK_F6, CTRL_DOWN_MASK);
      check(tool.mode == SelectionTool.SELECT_MINUS, "unrelated release preserves subtract-selection gesture");
      key(KEY_RELEASED, VK_CONTROL, 0);
      check(tool.mode == SelectionTool.NONE, "Ctrl release cancels subtract-selection gesture");
   }

   private static void shapeEvents() {
      for (int index : new int[]{RECTANGLE_ALGORITHMIC_TOOL_INDEX, ELLIPSE_ALGORITHMIC_TOOL_INDEX}) {
         app.setTool(index);
         begin(plate.getScreenPointFor(5.2, 4.2), plate.getScreenPointFor(16.2, 8.2), SHIFT_DOWN_MASK);
         String constrained = preview();
         key(KEY_RELEASED, VK_F6, SHIFT_DOWN_MASK);
         check(Tool.shiftDown && constrained.equals(preview()), "shape constraint survives unrelated release: " + current().getName());
         key(KEY_RELEASED, VK_SHIFT, 0);
         check(!constrained.equals(preview()), "shape constraint releases: " + current().getName());
         key(KEY_PRESSED, VK_ESCAPE, 0);
      }
   }

   private static void textAndFigletEvents() {
      app.setTool(TEXT_TOOL_INDEX);
      plate.getDocument().getCursorLocation().move(5, 5);
      key(KEY_PRESSED, VK_RIGHT, SHIFT_DOWN_MASK);
      key(KEY_RELEASED, VK_RIGHT, SHIFT_DOWN_MASK);
      check(Tool.shiftDown && plate.hasSelection(), "text selection retains Shift after arrow release");
      int width = plate.getSelectionRegion().width;
      key(KEY_PRESSED, VK_RIGHT, SHIFT_DOWN_MASK);
      key(KEY_RELEASED, VK_RIGHT, SHIFT_DOWN_MASK);
      check(plate.getSelectionRegion().width == width + 1, "held Shift extends text selection on next arrow");
      key(KEY_PRESSED, VK_ESCAPE, 0);
      app.setTool(FIGLET_TOOL_INDEX);
      Point origin = plate.getScreenPointFor(5.2, 10.2);
      mouse(MOUSE_PRESSED, origin, 0);
      mouse(MOUSE_RELEASED, origin, 0);
      var fonts = ((FigletToolOptionsPanel)current().getInlineOptionsPanel()).getFontModel();
      String originalFont = fonts.getFont().getName();
      key(KEY_PRESSED, VK_UP, ALT_DOWN_MASK);
      key(KEY_RELEASED, VK_UP, ALT_DOWN_MASK);
      check(Tool.altDown, "FIGlet retains Alt after font shortcut release");
      check(!originalFont.equals(fonts.getFont().getName()), "FIGlet Alt-Up selects next font");
      key(KEY_PRESSED, VK_DOWN, ALT_DOWN_MASK);
      key(KEY_RELEASED, VK_DOWN, ALT_DOWN_MASK);
      check(originalFont.equals(fonts.getFont().getName()), "FIGlet Alt-Down returns to previous font");
      key(KEY_RELEASED, VK_ALT, 0);
   }

   private static void nativeInput() throws Exception {
      edt(() -> {
         plate.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
         plate.requestFocusInWindow();
         return null;
      });
      robot.waitForIdle();
      await(() -> plate.isFocusOwner(), "native canvas focused after routed checks");
      Point start = edt(() -> plate.getScreenPointFor(5.12, 5.18));
      Point end = edt(() -> plate.getScreenPointFor(26.87, 11.81));
      Point screen = edt(() -> plate.getLocationOnScreen());
      robot.mouseMove(screen.x + start.x, screen.y + start.y);
      robot.mousePress(BUTTON1_DOWN_MASK);
      robot.mouseMove(screen.x + end.x, screen.y + end.y);
      robot.waitForIdle();
      String raw = edt(DrawingModifiersUiProbe::preview);
      robot.keyPress(VK_CONTROL);
      await(() -> Tool.controlDown && !Tool.altDown, "native Ctrl press");
      String originSnap = edt(DrawingModifiersUiProbe::preview);
      robot.keyPress(VK_ALT);
      await(() -> Tool.controlDown && Tool.altDown, "native combined modifiers");
      String bothSnap = edt(DrawingModifiersUiProbe::preview);
      robot.keyRelease(VK_CONTROL);
      await(() -> !Tool.controlDown && Tool.altDown, "native Ctrl release preserves Alt");
      robot.keyRelease(VK_ALT);
      await(() -> !Tool.controlDown && !Tool.altDown, "native Alt release");
      check(raw.equals(edt(DrawingModifiersUiProbe::preview)), "native stationary release restores raw preview with arrowheads");
      check(!raw.equals(originSnap) && !raw.equals(bothSnap), "native snaps visibly change rendered line");
      robot.keyPress(VK_CONTROL);
      robot.keyPress(VK_ALT);
      robot.keyRelease(VK_ALT);
      await(() -> Tool.controlDown && !Tool.altDown, "native Alt release preserves Ctrl");
      check(originSnap.equals(edt(DrawingModifiersUiProbe::preview)), "native reverse release order restores origin-only snap");
      robot.keyPress(VK_ALT);
      robot.waitForIdle();
      String commit = edt(DrawingModifiersUiProbe::preview);
      Files.createDirectories(OUTPUT);
      ImageIO.write(robot.createScreenCapture(edt(() -> frame.getBounds())), "png", OUTPUT.resolve("both-endpoints-snapped.png").toFile());
      robot.mouseRelease(BUTTON1_DOWN_MASK);
      await(() -> current().markPlate == null, "native mouse release commits");
      check(commit.equals(edt(() -> plate.getContent().toString())), "native preview and committed arrowed line match");
      check(edt(() -> Tool.controlDown && Tool.altDown), "native mouse release preserves modifiers");
      edt(() -> { plate.getContent().clear(); return null; });
      robot.mouseMove(screen.x + start.x, screen.y + start.y);
      robot.mousePress(BUTTON1_DOWN_MASK);
      robot.mouseMove(screen.x + end.x, screen.y + end.y);
      robot.waitForIdle();
      check(commit.equals(edt(DrawingModifiersUiProbe::preview)), "native modifiers held before next drag snap both endpoints");
      robot.mouseRelease(BUTTON1_DOWN_MASK);
      await(() -> current().markPlate == null, "native second drag commits");
      robot.keyRelease(VK_CONTROL);
      robot.keyRelease(VK_ALT);
      robot.waitForIdle();
   }

   private static Tool current() { return app.getMainPanel().getCurrentTool(); }

   private static void begin(Point start, Point end, int modifiers) {
      mouse(MOUSE_PRESSED, start, modifiers);
      mouse(MOUSE_DRAGGED, end, modifiers);
   }

   private static void mouse(int id, Point point, int modifiers) {
      boolean released = id == MOUSE_RELEASED;
      MouseEvent event = new MouseEvent(plate, id, System.currentTimeMillis(), modifiers | (released ? 0 : BUTTON1_DOWN_MASK),
         point.x, point.y, 1, false, id == MOUSE_DRAGGED ? NOBUTTON : BUTTON1);
      switch (id) {
         case MOUSE_PRESSED -> plate.mousePressed(event);
         case MOUSE_DRAGGED -> plate.mouseDragged(event);
         case MOUSE_RELEASED -> plate.mouseReleased(event);
         default -> throw new AssertionError(id);
      }
   }

   private static void key(int id, int code, int modifiers) {
      KeyEvent event = new KeyEvent(plate, id, System.currentTimeMillis(), modifiers, code, CHAR_UNDEFINED);
      if (id == KEY_PRESSED) plate.keyPressed(event); else plate.keyReleased(event);
   }

   private static String preview() {
      BufferedImage image = new BufferedImage(plate.getWidth(), plate.getHeight(), BufferedImage.TYPE_INT_RGB);
      Graphics2D graphics = image.createGraphics();
      try { plate.paint(graphics); } finally { graphics.dispose(); }
      CharacterPlate result = new CharacterPlate(plate.getDocumentSize());
      current().markPlate.convert().pasteInto(result);
      return result.toString();
   }

   private static String expectedLine(Point start, Point end, int modifiers) {
      Point2d from = plate.getRealLocationForScreenPoint(start);
      Point2d to = plate.getRealLocationForScreenPoint(end);
      if ((modifiers & CTRL_DOWN_MASK) != 0) from.moveTo(Math.floor(from.getX()) + 0.5, Math.floor(from.getY()) + 0.5);
      if ((modifiers & ALT_DOWN_MASK) != 0) to.moveTo(Math.floor(to.getX()) + 0.5, Math.floor(to.getY()) + 0.5);
      from.translate(-0.5, -0.5);
      to.translate(-0.5, -0.5);
      FilterMatrix[] empty = new FilterMatrix[0];
      PixelPlate expected = new PixelPlate(new Rectangle(plate.getDocumentSize()), new Filter(empty, empty, empty, empty, empty));
      expected.setMode(PixelPlateMode.CHAR);
      LineAlgorithm.drawLine(expected, from, to, AlgorithmicLineStyle.VERONICA, false);
      CharacterPlate result = new CharacterPlate(plate.getDocumentSize());
      expected.convert().pasteInto(result);
      return result.toString();
   }

   private static void arrowPlacement(ArrowheadPlacement placement) {
      JComboBox<?> combo = findArrowCombo(current().getInlineOptionsPanel().getContent());
      combo.setSelectedItem(placement);
   }

   private static JComboBox<?> findArrowCombo(Component component) {
      if (component instanceof JComboBox<?> combo && combo.getItemAt(0) instanceof ArrowheadPlacement) return combo;
      if (component instanceof Container container) {
         for (Component child : container.getComponents()) {
            JComboBox<?> found = findArrowCombo(child);
            if (found != null) return found;
         }
      }
      return null;
   }

   private static void await(Supplier<Boolean> condition, String message) throws Exception {
      long deadline = System.nanoTime() + 3_000_000_000L;
      while (!edt(condition) && System.nanoTime() < deadline) Thread.sleep(25);
      check(edt(condition), message);
   }

   private static void check(boolean condition, String message) {
      if (!condition) throw new AssertionError(message);
      System.out.println("OK " + message);
   }

   private static <T> T edt(Supplier<T> action) throws Exception {
      FutureTask<T> task = new FutureTask<>(action::get);
      SwingUtilities.invokeAndWait(task);
      return task.get();
   }
}
