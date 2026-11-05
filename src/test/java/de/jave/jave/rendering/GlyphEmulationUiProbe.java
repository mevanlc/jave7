package de.jave.jave.rendering;

import de.jave.jave.JavEApplication;
import de.jave.jave.Plate;
import de.jave.jave.actions.preferences.IJavePreferencesPanel;
import de.jave.jave.application.startup.ConfigurationList;
import de.jave.jave.menu.ProbePreferencesFactory;
import de.jave.jave.preferences.GlyphEmulation;
import de.jave.jave.preferences.PlatePreferences;
import de.jave.lib.CharacterPlate;
import de.jave.preferences.JavePreferences;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Explicit native GUI check, using isolated preferences, documents and recovery files. */
public final class GlyphEmulationUiProbe {
   private static final Path OUTPUT = Path.of(System.getProperty("jave.probeOutput"));
   private static JavEApplication app;
   private static Plate plate;
   private static Robot robot;

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
               app.getApplicationPreferences().getDisplayFontModel().setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
               app.doNew();
               plate = app.getMainPanel().getPlate();
               plate.getPlatePreferences().getGridVisibilityModel().setValue(false);
               plate.setContent(new CharacterPlate(new String[]{
                  "Jave / Kitty built-in glyph comparison",
                  "┌─────────┬─────────┐ ╔═══════════╗",
                  "│  Box    │ Drawing │ ║  Double   ║",
                  "├─────────┼─────────┤ ╠═══════════╣",
                  "└─────────┴─────────┘ ╚═══════════╝",
                  "╭─────────╮ ╱╲╳  ◉○●◖◗ ◜◝◞◟◠◡",
                  "╰─────────╯ ▀▄█▌▐▖▗▘▝▙▚▛▜▞▟ ░▒▓",
                  "⠁⠂⠄⡀⠈⠐⠠⢀ ⠿⣿ ⣀⣤⣶⣿ ⡇⢸",
                  "\uE0B0\uE0B1\uE0B2\uE0B3\uE0B4\uE0B5\uE0B6\uE0B7 \uEE00\uEE01\uEE02 \uEE03\uEE04\uEE05 \uF5EE\uF5EF\uF60C\uF60D",
                  new String(new int[]{0x1CD00, 0x1CD01, 0x1CDE5, 0x1FB00, 0x1FB01, 0x1FB3C, 0x1FBAE, 0x1CC21, 0x1CE51, 0x1CE90, 0x1CC30, 0x1FBEC}, 0, 12)
               }));
               app.getFrame().setBounds(100, 60, 1150, 850);
               app.getFrame().setVisible(true);
               app.getFrame().toFront();
               Desktop.getDesktop().requestForeground(true);
               plate.requestFocusInWindow();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
            return null;
         });
         robot = new Robot();
         robot.waitForIdle();
         check(edt(() -> plate.getPlatePreferences().getGlyphEmulation()) == GlyphEmulation.JAVE, "default Jave");
         capture("jave-default.png");
         preferences(GlyphEmulation.KITTY, "OK", true);
         check(edt(() -> plate.getPlatePreferences().getGlyphEmulation()) == GlyphEmulation.KITTY, "OK updates open canvas");
         check(edt(() -> new PlatePreferences(new JavePreferences()).getGlyphEmulation()) == GlyphEmulation.KITTY, "Kitty is persisted");
         capture("kitty-canvas.png");
         edt(() -> {
            plate.setSelection(new Rectangle(0, 6, 20, 1));
            plate.getDocument().getSelection().set3dView(false);
            return null;
         });
         capture("kitty-selection.png");
         preferences(GlyphEmulation.JAVE, "Cancel", false);
         check(edt(() -> plate.getPlatePreferences().getGlyphEmulation()) == GlyphEmulation.KITTY, "Cancel preserves Kitty");
         atlas();
         System.out.println("ALL GLYPH EMULATION UI PROBE CHECKS PASSED");
      } catch (Throwable error) {
         error.printStackTrace();
         status = 1;
      } finally {
         if (app != null) edt(() -> { app.dispose(); return null; });
      }
      System.exit(status);
   }

   private static void preferences(GlyphEmulation choice, String button, boolean screenshot) throws Exception {
      SwingUtilities.invokeLater(() -> app.getMaxOsXApplicationCallbacks().performShowPreferencesDialog());
      JDialog dialog = null;
      long deadline = System.nanoTime() + 10_000_000_000L;
      while (dialog == null && System.nanoTime() < deadline) {
         dialog = edt(() -> {
            for (Window window : Window.getWindows()) {
               if (window instanceof JDialog candidate && candidate.isShowing() && "Preferences".equals(candidate.getTitle())) return candidate;
            }
            return null;
         });
         if (dialog == null) Thread.sleep(25);
      }
      check(dialog != null, "Preferences dialog opens");
      JDialog active = dialog;
      edt(() -> {
         for (Component component : descendants(active)) {
            if (component instanceof JList<?> list) {
               for (int i = 0; i < list.getModel().getSize(); i++) {
                  if (list.getModel().getElementAt(i) instanceof IJavePreferencesPanel panel && "Display".equals(panel.getTitle())) list.setSelectedIndex(i);
               }
            }
         }
         for (Component component : descendants(active)) {
            if (component instanceof JLabel label && "Glyph emulation:".equals(label.getText())) {
               ((JComboBox<?>)label.getLabelFor()).setSelectedItem(choice);
               return null;
            }
         }
         throw new AssertionError("Missing Glyph emulation dropdown");
      });
      robot.waitForIdle();
      robot.delay(200);
      if (screenshot) ImageIO.write(robot.createScreenCapture(edt(active::getBounds)), "png", OUTPUT.resolve("display-preferences.png").toFile());
      edt(() -> {
         for (Component component : descendants(active)) {
            if (component instanceof JButton candidate && button.equals(candidate.getText())) {
               candidate.doClick();
               return null;
            }
         }
         throw new AssertionError("Missing " + button);
      });
      robot.waitForIdle();
   }

   private static List<Component> descendants(Container container) {
      List<Component> result = new ArrayList<>();
      for (Component child : container.getComponents()) {
         result.add(child);
         if (child instanceof Container nested) result.addAll(descendants(nested));
      }
      return result;
   }

   private static void capture(String name) throws Exception {
      robot.waitForIdle();
      robot.delay(200);
      ImageIO.write(robot.createScreenCapture(edt(() -> app.getFrame().getBounds())), "png", OUTPUT.resolve(name).toFile());
   }

   private static void atlas() throws Exception {
      List<Integer> codePoints = KittyGlyphRenderer.supportedCodePoints().stream().sorted().toList();
      int columns = 32;
      int tileWidth = 40;
      int tileHeight = 80;
      BufferedImage image = new BufferedImage(columns * tileWidth, ((codePoints.size() + columns - 1) / columns) * tileHeight, BufferedImage.TYPE_INT_RGB);
      var graphics = image.createGraphics();
      try {
         graphics.setColor(Color.WHITE);
         graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
         graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 9));
         for (int i = 0; i < codePoints.size(); i++) {
            int x = i % columns * tileWidth;
            int y = i / columns * tileHeight;
            graphics.setColor(new Color(20, 70, 110));
            KittyGlyphRenderer.draw(graphics, codePoints.get(i), x + 4, y + 3, 32, 60);
            graphics.setColor(Color.GRAY);
            graphics.drawString(Integer.toHexString(codePoints.get(i)), x + 3, y + 76);
         }
      } finally {
         graphics.dispose();
      }
      ImageIO.write(image, "png", OUTPUT.resolve("kitty-glyph-atlas.png").toFile());
   }

   private static <T> T edt(Supplier<T> action) throws Exception {
      FutureTask<T> task = new FutureTask<>(action::get);
      SwingUtilities.invokeAndWait(task);
      return task.get();
   }

   private static void check(boolean value, String message) {
      if (!value) throw new AssertionError(message);
      System.out.println("PASS: " + message);
   }
}
