package de.jave.jave;

import de.jave.ascii.plate.CellScalingMode;
import de.jave.ascii.plate.CharacterMetrics;
import de.jave.jave.actions.JaveKeyBindings;
import de.jave.jave.actions.ZoomInAction;
import de.jave.jave.application.startup.ConfigurationList;
import de.jave.jave.menu.ProbePreferencesFactory;
import de.jave.jave.preferences.PlatePreferences;
import de.jave.lib.CharacterPlate;
import de.jave.preferences.JavePreferences;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Explicit GUI check; all preferences, documents and recovery files are isolated. */
public final class AutoZoomUiProbe {
   private static JavEApplication app;
   private static Plate plate;
   private static JFrame frame;
   private static Robot robot;
   private static JCheckBoxMenuItem autoItem;

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
               frame.setBounds(100, 60, 1100, 740);
               frame.setAlwaysOnTop(true);
               frame.setVisible(true);
               frame.toFront();
               Desktop.getDesktop().requestForeground(true);
               plate.requestFocusInWindow();
               autoItem = (JCheckBoxMenuItem)viewItem("Auto Zoom");
               return null;
            } catch (Exception error) { throw new RuntimeException(error); }
         });
         robot = new Robot();
         robot.setAutoDelay(70);
         await(() -> plate.isFocusOwner(), "native canvas focused");
         check(!edt(() -> autoItem.isSelected()), "Auto Zoom defaults off");
         edt(() -> {
            for (int n = 0; n < 25; n++) plate.getZoomableFontModel().zoomIn();
            frame.validate();
            return null;
         });
         await(() -> scrollPane().getHorizontalScrollBar().isVisible() && scrollPane().getVerticalScrollBar().isVisible(), "oversized manual zoom shows scrollbars");
         edt(() -> { autoItem.doClick(); return null; });
         fit("enabling fits and removes existing scrollbars");
         check(edt(() -> new PlatePreferences(new JavePreferences()).getAutoZoomModel().getValue()), "enabled preference is saved");
         edt(() -> { frame.setSize(760, 540); frame.validate(); return null; });
         fit("smaller window");
         edt(() -> { frame.setSize(1180, 790); frame.validate(); return null; });
         fit("larger window");
         edt(() -> { app.setLayersPanelVisible(true); frame.validate(); return null; });
         fit("layers panel open");
         edt(() -> { app.setLayersPanelVisible(false); app.getActions().getToolsPaletteToggleAction().setSelected(false); frame.validate(); return null; });
         fit("layers and tools panels closed");
         edt(() -> { app.getActions().getRulerToggleAction().setSelected(false); frame.validate(); return null; });
         fit("rulers hidden");
         edt(() -> { app.getActions().getRulerToggleAction().setSelected(true); frame.validate(); return null; });
         fit("rulers restored");
         edt(() -> { app.getApplicationPreferences().getDisplayFontModel().setFontSize(18); frame.validate(); return null; });
         fit("display font changed");
         edt(() -> {
            plate.getPlatePreferences().setCellScalingMode(CellScalingMode.SCALED);
            plate.getPlatePreferences().setCellScalingWidth(1.3F);
            plate.getPlatePreferences().setCellScalingHeight(1.2F);
            frame.validate();
            return null;
         });
         fit("custom cell scaling");
         edt(() -> {
            plate.getPlatePreferences().setCellScalingMode(CellScalingMode.LINE);
            app.getApplicationPreferences().getDisplayFontModel().setFontSize(13);
            plate.setContent(new CharacterPlate(120, 45));
            frame.validate();
            return null;
         });
         fit("canvas enlarged");
         edt(() -> { plate.setContent(new CharacterPlate(1, 1)); frame.validate(); return null; });
         await(() -> plate.getZoomableFontModel().getSizeDelta() == 100, "maximum auto zoom is +100");
         fit("small canvas respects maximum");
         edt(() -> { plate.setContent(new CharacterPlate(1000, 800)); frame.validate(); return null; });
         await(() -> plate.getZoomableFontModel().getSizeDelta() == -10, "minimum auto zoom is -10");
         await(() -> scrollPane().getHorizontalScrollBar().isVisible(), "scrolling remains when minimum cannot fit");
         edt(() -> { plate.setContent(new CharacterPlate(71, 30)); frame.validate(); return null; });
         fit("canvas shrink removes scrolling again");

         int before = edt(() -> plate.getZoomableFontModel().getSizeDelta());
         edt(() -> { viewItem("Zoom out").doClick(); return null; });
         check(edt(() -> !autoItem.isSelected() && plate.getZoomableFontModel().getSizeDelta() == before - 1), "manual menu zoom unchecks Auto Zoom and performs one step");
         edt(() -> { frame.setSize(850, 620); frame.validate(); return null; });
         robot.waitForIdle();
         check(edt(() -> plate.getZoomableFontModel().getSizeDelta() == before - 1), "resize leaves manual zoom unchanged");
         check(!edt(() -> new PlatePreferences(new JavePreferences()).getAutoZoomModel().getValue()), "manual override saves unchecked preference");

         edt(() -> { autoItem.doClick(); return null; });
         fit("re-enabled after manual zoom");
         int toolbarBefore = edt(() -> plate.getZoomableFontModel().getSizeDelta());
         edt(() -> { new ZoomInAction(app.getMainPanel()).execute(frame); return null; });
         check(edt(() -> !autoItem.isSelected() && plate.getZoomableFontModel().getSizeDelta() == toolbarBefore + 1), "toolbar zoom action disables Auto Zoom and increments");
         edt(() -> { autoItem.doClick(); plate.requestFocusInWindow(); return null; });
         fit("re-enabled before native shortcut");
         int shortcutBefore = edt(() -> plate.getZoomableFontModel().getSizeDelta());
         edt(() -> {
            frame.toFront();
            Desktop.getDesktop().requestForeground(true);
            plate.requestFocusInWindow();
            return null;
         });
         robot.waitForIdle();
         Point click = edt(() -> {
            Point point = plate.getScreenPointFor(4.5, 4.5);
            SwingUtilities.convertPointToScreen(point, plate);
            return point;
         });
         robot.mouseMove(click.x, click.y);
         robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
         robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
         robot.waitForIdle();
         await(() -> plate.isFocusOwner(), "canvas focused for native shortcut");
         int menuModifier = (JaveKeyBindings.ZOOM_OUT.getModifiers() & InputEvent.META_DOWN_MASK) != 0 ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;
         robot.keyPress(menuModifier);
         robot.keyPress(KeyEvent.VK_MINUS);
         robot.keyRelease(KeyEvent.VK_MINUS);
         robot.keyRelease(menuModifier);
         await(() -> !autoItem.isSelected() && plate.getZoomableFontModel().getSizeDelta() == shortcutBefore - 1, "native zoom shortcut unchecks and zooms out");
         edt(() -> { autoItem.doClick(); return null; });
         fit("re-enabled before direct canvas shortcut");
         int canvasBefore = edt(() -> plate.getZoomableFontModel().getSizeDelta());
         edt(() -> {
            plate.keyPressed(new KeyEvent(plate, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), InputEvent.CTRL_DOWN_MASK, KeyEvent.VK_EQUALS, '+'));
            plate.keyReleased(new KeyEvent(plate, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_CONTROL, KeyEvent.CHAR_UNDEFINED));
            return null;
         });
         check(edt(() -> !autoItem.isSelected() && plate.getZoomableFontModel().getSizeDelta() == canvasBefore + 1), "canvas Ctrl-plus disables Auto Zoom and increments");

         edt(() -> { autoItem.doClick(); return null; });
         fit("re-enabled before tab changes");
         Plate first = plate;
         edt(() -> {
            app.doNew();
            plate = app.getMainPanel().getPlate();
            plate.setContent(new CharacterPlate(30, 15));
            frame.validate();
            return null;
         });
         fit("new document inherits Auto Zoom");
         edt(() -> { app.doNextDocument(); plate = app.getMainPanel().getPlate(); frame.validate(); return null; });
         check(plate == first, "returned to original document");
         fit("returning to existing tab refits its canvas");
         int fixed = edt(() -> plate.getZoomableFontModel().getSizeDelta());
         edt(() -> { autoItem.doClick(); frame.setSize(1000, 720); frame.validate(); return null; });
         robot.waitForIdle();
         check(edt(() -> plate.getZoomableFontModel().getSizeDelta() == fixed), "unchecking preserves current zoom");
         edt(() -> { autoItem.doClick(); return null; });
         fit("final fit");
         Path output = Path.of(System.getProperty("jave.probeOutput"));
         Files.createDirectories(output);
         ImageIO.write(robot.createScreenCapture(edt(() -> frame.getBounds())), "png", output.resolve("auto-zoom-fit.png").toFile());
         System.out.println("ALL AUTO ZOOM PROBE CHECKS PASSED");
      } catch (Throwable error) {
         error.printStackTrace();
         status = 1;
      } finally {
         if (frame != null) edt(() -> { frame.dispose(); return null; });
      }
      System.exit(status);
   }

   private static JScrollPane scrollPane() {
      return (JScrollPane)SwingUtilities.getAncestorOfClass(JScrollPane.class, plate);
   }

   private static JMenuItem viewItem(String text) {
      for (int i = 0; i < frame.getJMenuBar().getMenuCount(); i++) {
         JMenu menu = frame.getJMenuBar().getMenu(i);
         if (menu == null || !"View".equals(menu.getText())) continue;
         for (int j = 0; j < menu.getItemCount(); j++) {
            JMenuItem item = menu.getItem(j);
            if (item != null && text.equals(item.getText())) return item;
         }
      }
      throw new AssertionError("Missing View > " + text);
   }

   private static boolean bestFit() {
      if (!autoItem.isSelected()) return false;
      JScrollPane scroll = scrollPane();
      Dimension extent = scroll.getViewport().getExtentSize();
      Dimension preferred = plate.getPreferredSize();
      if (preferred.width > extent.width || preferred.height > extent.height) return false;
      if (scroll.getVerticalScrollBar().isVisible() || scroll.getHorizontalScrollBar().isVisible()) return false;
      Rectangle visible = scroll.getViewport().getViewRect();
      Point origin = plate.getScreenPointFor(0, 0);
      if (!visible.contains(new Rectangle(origin.x - 1, origin.y - 1, preferred.width, preferred.height))) return false;
      ZoomableFontModel model = plate.getZoomableFontModel();
      int delta = model.getSizeDelta();
      if (delta < -10 || delta > 100) return false;
      if (delta == 100) return true;
      Font larger = model.getOriginalFont().deriveFont((float)model.getOriginalFont().getSize() + delta + 1);
      CharacterMetrics next = CharacterMetrics.createCharacterMetrics(larger);
      return !AutoZoom.fits(plate.getDocumentSize(), next, extent);
   }

   private static void fit(String message) throws Exception {
      await(AutoZoomUiProbe::bestFit, message);
      System.out.println("  delta=" + edt(() -> plate.getZoomableFontModel().getSizeDelta()) + " viewport=" + edt(() -> scrollPane().getViewport().getExtentSize()));
   }

   private static void await(Supplier<Boolean> condition, String message) throws Exception {
      long deadline = System.nanoTime() + 5_000_000_000L;
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
