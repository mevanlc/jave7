package de.jave.jave;

import de.jave.gui.GStatusLabel;
import de.jave.jave.application.startup.ConfigurationList;
import de.jave.jave.menu.ProbePreferencesFactory;
import de.jave.lib.area.BooleanArea;
import java.awt.Desktop;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Native picker focus and input checks with isolated preferences and documents. */
public final class SelectionAndPickerUiProbe {
   private static JavEApplication app;
   private static Plate plate;
   private static Robot robot;
   private static GStatusLabel selectionLabel;
   private static final Path OUTPUT = Path.of(System.getProperty("jave.probeOutput"));

   public static void main(String[] args) throws Exception {
      int status = 0;
      try {
         System.setProperty("java.util.prefs.PreferencesFactory", ProbePreferencesFactory.class.getName());
         Files.createDirectories(OUTPUT);
         edt(() -> {
            try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
               var init = Class.forName("de.jave.jave.application.startup.JaveStartup").getDeclaredMethod("initConfigFiles");
               init.setAccessible(true);
               app = new JavEApplication((ConfigurationList)init.invoke(null));
               app.startupMenuBar();
               app.doNew();
               plate = app.getMainPanel().getPlate();
               selectionLabel = (GStatusLabel)field(field(app, "statusBar"), "selectionSizeLabel");
               app.getFrame().setBounds(70, 60, 1100, 740);
               app.getFrame().setVisible(true);
               app.getFrame().toFront();
               Desktop.getDesktop().requestForeground(true);
               plate.requestFocusInWindow();
            } catch (Exception error) {
               throw new RuntimeException(error);
            }
            return null;
         });
         robot = new Robot();
         robot.setAutoDelay(45);
         await(() -> plate.isFocusOwner(), "canvas receives native focus");
         selectionChecks();
         pickerChecks();
         System.out.println("ALL SELECTION AND PICKER UI PROBE CHECKS PASSED");
      } catch (Throwable error) {
         error.printStackTrace();
         if (robot != null && app != null) capture("failure.png");
         status = 1;
      } finally {
         edt(() -> {
            for (Window window : Window.getWindows()) window.dispose();
            if (app != null) app.dispose();
            return null;
         });
      }
      System.exit(status);
   }

   private static void selectionChecks() throws Exception {
      check(edt(() -> !selectionLabel.isVisible()), "selection size starts hidden");
      edt(() -> {
         plate.setSelection(new Rectangle(3, 4, 7, 5));
         plate.saveCurrentState("select");
         return null;
      });
      selectionSize("7 x 5");
      edt(() -> { plate.getSelection().resizeByCursorE_RIGHT(); return null; });
      selectionSize("8 x 5");
      edt(() -> { plate.getSelection().resizeByCursorS_DOWN(); plate.saveCurrentState("resize selection"); return null; });
      selectionSize("8 x 6");
      edt(() -> { app.doUndo(); return null; });
      selectionSize("7 x 5");
      edt(() -> { app.doRedo(); return null; });
      selectionSize("8 x 6");
      edt(() -> {
         BooleanArea mask = new BooleanArea(9, 6);
         mask.set(0, 0, true);
         mask.set(8, 5, true);
         plate.getSelection().set(new Rectangle(2, 2, 9, 6), mask);
         return null;
      });
      selectionSize("9 x 6");
      edt(() -> { app.doNew(); return null; });
      check(edt(() -> !selectionLabel.isVisible()), "new document has no stale selection size");
      edt(() -> { app.setCurrentDocument(0); return null; });
      selectionSize("9 x 6");
      robot.waitForIdle();
      capture("selection-size.png");
      edt(() -> { plate.getSelection().delete(); plate.repaint(); return null; });
      check(edt(() -> !selectionLabel.isVisible()), "clearing selection hides size");
   }

   private static void pickerChecks() throws Exception {
      edt(() -> {
         app.switchToTextTool();
         plate.getDocument().getCursorLocation().setLocation(3, 3);
         JaveGlobalRessources.cursorBlink = false;
         plate.requestFocusInWindow();
         return null;
      });
      await(() -> plate.isFocusOwner() && cursorVisible(), "text caret visible on canvas");
      UnicodePickerDialog unicode = edt(() -> new UnicodePickerDialog(app.getFrame(), app.getMainPanel()));
      JDialog unicodeWindow = (JDialog)field(unicode, "dialog");
      JTextField search = (JTextField)field(unicode, "searchField");
      JList<?> entries = (JList<?>)field(unicode, "entryList");
      edt(() -> { unicode.show(); unicodeWindow.setLocation(760, 100); return null; });
      await(() -> search.isFocusOwner() && cursorVisible(), "Unicode search keeps canvas caret");
      edt(() -> { search.setText("U+2500"); return null; });
      await(() -> entries.getModel().getSize() == 1, "Unicode search loads requested row");
      Point row = edt(() -> {
         Rectangle bounds = entries.getCellBounds(0, 0);
         Point point = new Point(bounds.x + 30, bounds.y + bounds.height / 2);
         SwingUtilities.convertPointToScreen(point, entries);
         return point;
      });
      click(row, InputEvent.BUTTON1_DOWN_MASK);
      check(edt(() -> plate.getChar(3, 3) == ' '), "single click only selects Unicode row");
      robot.delay(600);
      doubleClick(row, InputEvent.BUTTON1_DOWN_MASK);
      await(() -> plate.getChar(3, 3) == '─', "native double click inserts at caret");
      check(edt(() -> unicodeWindow.isShowing() && entries.isFocusOwner() && cursorVisible()), "double click leaves picker focused with caret visible");
      edt(() -> { plate.undo(); return null; });
      check(edt(() -> plate.getChar(3, 3) == ' '), "one undo removes double-click insertion");
      edt(() -> { plate.redo(); return null; });
      check(edt(() -> plate.getChar(3, 3) == '─'), "redo restores Unicode insertion");
      edt(() -> { plate.setCharForce(new Point(3, 3), ' '); return null; });
      Point blank = edt(() -> {
         Point point = new Point(30, entries.getCellBounds(0, 0).height + 30);
         SwingUtilities.convertPointToScreen(point, entries);
         return point;
      });
      doubleClick(blank, InputEvent.BUTTON1_DOWN_MASK);
      check(edt(() -> plate.getChar(3, 3) == ' '), "double click below Unicode rows does not insert");
      doubleClick(row, InputEvent.BUTTON3_DOWN_MASK);
      check(edt(() -> plate.getChar(3, 3) == ' '), "right double click does not insert");
      capture("unicode-picker.png");

      BoxDrawingPickerDialog box = edt(() -> new BoxDrawingPickerDialog(app.getFrame(), app.getMainPanel()));
      JDialog boxWindow = (JDialog)field(box, "dialog");
      edt(() -> { box.show(); boxWindow.setLocation(900, 100); return null; });
      await(() -> boxWindow.isFocused() && cursorVisible(), "Box Drawing Picker keeps canvas caret");
      edt(() -> { JaveGlobalRessources.cursorBlink = true; return null; });
      await(() -> boxWindow.isFocused() && !cursorVisible(), "caret blink hides while picker stays focused");
      await(() -> boxWindow.isFocused() && cursorVisible(), "caret blink redraws while picker stays focused");
      edt(() -> { JaveGlobalRessources.cursorBlink = false; return null; });
      robot.keyPress(KeyEvent.VK_ENTER);
      robot.keyRelease(KeyEvent.VK_ENTER);
      await(() -> plate.getChar(3, 3) == '┌' && cursorVisible(), "box insertion retains caret and picker focus");
      capture("box-drawing-picker.png");
      edt(() -> { boxWindow.setVisible(false); unicodeWindow.toFront(); search.requestFocusInWindow(); return null; });
      await(() -> search.isFocusOwner() && cursorVisible(), "switching back to Unicode keeps caret");
      JDialog other = edt(() -> {
         JDialog dialog = new JDialog(app.getFrame(), "Other dialog", false);
         dialog.add(new JTextField("Unrelated focus"));
         dialog.setBounds(800, 400, 250, 100);
         dialog.setVisible(true);
         return dialog;
      });
      await(() -> other.isFocused() && !cursorVisible(), "unrelated dialog hides canvas caret");
      edt(() -> {
         other.dispose();
         unicodeWindow.setVisible(false);
         app.getFrame().toFront();
         plate.requestFocusInWindow();
         return null;
      });
      await(() -> plate.isFocusOwner() && cursorVisible(), "closing pickers restores canvas caret");
   }

   private static boolean cursorVisible() {
      return field(plate, "xorPainter") != null;
   }

   private static Object field(Object object, String name) {
      try {
         Field field = object.getClass().getDeclaredField(name);
         field.setAccessible(true);
         return field.get(object);
      } catch (ReflectiveOperationException error) {
         throw new RuntimeException(error);
      }
   }

   private static void selectionSize(String size) throws Exception {
      check(edt(() -> selectionLabel.isVisible() && ("Selection: " + size).equals(field(selectionLabel, "text"))), "selection size " + size);
   }

   private static void click(Point point, int button) {
      robot.mouseMove(point.x, point.y);
      robot.mousePress(button);
      robot.mouseRelease(button);
      robot.waitForIdle();
   }

   private static void doubleClick(Point point, int button) {
      robot.mouseMove(point.x, point.y);
      robot.mousePress(button);
      robot.mouseRelease(button);
      robot.mousePress(button);
      robot.mouseRelease(button);
      robot.waitForIdle();
   }

   private static void capture(String name) throws Exception {
      Rectangle bounds = edt(() -> app.getFrame().getBounds());
      ImageIO.write(robot.createScreenCapture(bounds), "png", OUTPUT.resolve(name).toFile());
   }

   private static void check(boolean condition, String message) {
      if (!condition) throw new AssertionError(message);
      System.out.println("PASS " + message);
   }

   private static void await(Supplier<Boolean> condition, String message) throws Exception {
      long deadline = System.nanoTime() + 8_000_000_000L;
      while (System.nanoTime() < deadline) {
         if (edt(condition)) { check(true, message); return; }
         Thread.sleep(50);
      }
      throw new AssertionError(message);
   }

   private static <T> T edt(Supplier<T> supplier) throws Exception {
      FutureTask<T> task = new FutureTask<>(supplier::get);
      SwingUtilities.invokeAndWait(task);
      return task.get();
   }
}
