package de.jave.jave.menu;

import de.jave.jave.JavEApplication;
import de.jave.jave.actions.ClipboardOverride;
import de.jave.jave.application.startup.ConfigurationList;
import java.awt.Component;
import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Run explicitly, outside the unit suite: it opens only its own app instance and uses isolated preferences. */
public final class MenuSearchUiProbe {
   private static JavEApplication app;
   private static JFrame frame;
   private static JTextField field;
   private static Robot robot;
   private static final Path OUTPUT = Path.of(System.getProperty("jave.probeOutput", "build"));

   public static void main(String[] args) throws Exception {
      try {
         run(args);
         System.exit(0);
      } catch (Throwable error) {
         error.printStackTrace();
         System.exit(1);
      }
   }

   private static void run(String[] args) throws Exception {
      System.setProperty("java.util.prefs.PreferencesFactory", ProbePreferencesFactory.class.getName());
      try {
         edt(() -> {
            try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
               JPopupMenu.setDefaultLightWeightPopupEnabled(false);
               var init = Class.forName("de.jave.jave.application.startup.JaveStartup").getDeclaredMethod("initConfigFiles");
               init.setAccessible(true);
               app = new JavEApplication((ConfigurationList)init.invoke(null));
               app.startupMenuBar();
               app.doNew();
               frame = app.getFrame();
               return null;
            } catch (Exception exception) {
               throw new RuntimeException(exception);
            }
         });
         if (args.length > 0 && args[0].equals("inventory")) {
            List<String> inventory = edt(() -> {
               List<String> values = new ArrayList<>();
               for (Component component : frame.getJMenuBar().getComponents()) {
                  if (component instanceof JMenu menu) {
                     inventory(menu, "", values);
                  }
               }
               return values;
            });
            Files.write(Path.of(args[1]), inventory);
            System.out.println("INVENTORY " + inventory.size());
            return;
         }
         edt(() -> {
            frame.setBounds(120, 100, 1100, 720);
            frame.setVisible(true);
            frame.toFront();
            field = (JTextField)find(frame.getJMenuBar(), component -> "menuSearchField".equals(component.getName()));
            app.getMainPanel().requestFocus();
            return null;
         });
         robot = new Robot();
         robot.setAutoDelay(40);
         robot.waitForIdle();
         await(() -> frame.isFocused() && app.getMainPanel().getPlate().isFocusOwner(), "probe canvas focused");
         searchShortcut();
         await(() -> field.isFocusOwner(), "shortcut focuses search field");
         query("grid");
         await(() -> list() != null && list().getModel().getSize() == 1, "grid result displayed");
         key(KeyEvent.VK_ENTER);
         await(() -> list().isFocusOwner() && list().getSelectedIndex() == 0, "Enter focuses first result");
         capture("build/menu-search-grid-preview.png");
         boolean before = edt(() -> app.getActions().getGridToggleAction().isSelected());
         key(KeyEvent.VK_ENTER);
         await(() -> app.getActions().getGridToggleAction().isSelected() != before && field.getText().isEmpty(), "second Enter executes grid toggle");
         await(() -> app.getMainPanel().getPlate().isFocusOwner(), "execution restores prior focus");
         searchShortcut();
         await(() -> field.isFocusOwner() && list().getModel().getElementAt(0) instanceof MenuEntry, "empty-query history");
         check(edt(() -> ((MenuEntry)list().getModel().getElementAt(0)).command().label().equals("Grid")), "history contains search invocation");
         query("layer");
         key(KeyEvent.VK_DOWN);
         await(() -> list().isFocusOwner(), "Down focuses results");
         key(KeyEvent.VK_UP);
         await(() -> field.isFocusOwner(), "Up returns from first result to field");
         chord(KeyEvent.VK_SHIFT, KeyEvent.VK_TAB);
         await(() -> list().getSelectedIndex() == selectableCount() - 1, "Shift-Tab wraps to last result");
         key(KeyEvent.VK_TAB);
         await(() -> field.isFocusOwner(), "Tab wraps to field");
         key(KeyEvent.VK_DOWN);
         key(KeyEvent.VK_S);
         await(() -> field.isFocusOwner() && field.getText().equals("layers"), "typing in results continues query");
         key(KeyEvent.VK_DOWN);
         key(KeyEvent.VK_BACK_SPACE);
         await(() -> field.isFocusOwner() && field.getText().equals("layer"), "Backspace in results edits query");
         query("hide layer");
         key(KeyEvent.VK_ENTER);
         await(() -> list().isFocusOwner(), "disabled item receives search focus");
         check(edt(() -> !((MenuEntry)list().getModel().getElementAt(0)).isEnabled()), "base-layer hide command disabled");
         capture("build/menu-search-disabled-preview.png");
         key(KeyEvent.VK_ENTER);
         check(edt(() -> field.getText().equals("hide layer") && list().isShowing()), "disabled Enter cannot execute");
         key(KeyEvent.VK_ESCAPE);
         await(() -> field.getText().isEmpty() && !list().isShowing(), "Escape clears and dismisses");
         searchShortcut();
         await(() -> field.isFocusOwner(), "search reopened");
         query("a");
         check(edt(() -> list().getModel().getSize() == 12 && list().getModel().getElementAt(11) instanceof String), "overflow notice occupies twelfth row");
         capture("build/menu-search-overflow.png");
         query("online documentation");
         key(KeyEvent.VK_ENTER);
         await(() -> list().isFocusOwner(), "Help result preview");
         check(edt(() -> {
            MenuEntry entry = (MenuEntry)list().getModel().getElementAt(0);
            Window resultWindow = SwingUtilities.getWindowAncestor(list());
            return !new Rectangle(entry.item().getLocationOnScreen(), entry.item().getSize()).intersects(resultWindow.getBounds());
         }), "Help preview shifted clear of search results");
         capture("build/menu-search-help-preview.png");
         key(KeyEvent.VK_ESCAPE);
         // Preview a three-level menu and a leaf with a disabled ancestor.
         searchShortcut();
         query("degrees right");
         key(KeyEvent.VK_ENTER);
         await(() -> list().isFocusOwner(), "nested result focused");
         check(edt(() -> ((MenuEntry)list().getSelectedValue()).parents().size() == 3
            && ((MenuEntry)list().getSelectedValue()).parents().stream().allMatch(menu -> menu.getPopupMenu().isShowing())),
            "all three actual menus revealed");
         capture("build/menu-search-nested-preview.png");
         query("use as brush");
         key(KeyEvent.VK_ENTER);
         await(() -> list().isFocusOwner(), "disabled ancestor result focused");
         check(edt(() -> {
            MenuEntry entry = (MenuEntry)list().getSelectedValue();
            return entry.item().isEnabled() && !entry.isEnabled() && entry.item().isShowing();
         }), "disabled Selection menu reveals enabled child without enabling execution");
         key(KeyEvent.VK_ENTER);
         check(edt(() -> list().isShowing()), "disabled ancestor blocks execution");
         key(KeyEvent.VK_ESCAPE);

         // Hover and a single click execute the real bound checkbox exactly once.
         boolean rulers = edt(() -> app.getActions().getRulerToggleAction().isSelected());
         searchShortcut();
         query("rulers");
         Point hover = edt(() -> {
            Point point = list().getLocationOnScreen();
            point.translate(list().getWidth() / 2, list().getFixedCellHeight() / 2);
            return point;
         });
         robot.mouseMove(hover.x, hover.y);
         robot.waitForIdle();
         await(() -> list().isFocusOwner() && list().getSelectedIndex() == 0, "hover focuses and reveals result");
         robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
         robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
         robot.waitForIdle();
         await(() -> app.getActions().getRulerToggleAction().isSelected() != rulers && !list().isShowing(), "single click executes toggle");
         robot.mouseMove(40, 60);
         searchShortcut();
         await(() -> field.isFocusOwner(), "mouse invocation history");
         check(edt(() -> ((MenuEntry)list().getModel().getElementAt(0)).command().label().equals("Rulers")), "mouse invocation is most recent");
         key(KeyEvent.VK_ESCAPE);

         // Exercise the actual Copy Action's focus delegate without modifying the system clipboard.
         int[] copies = {0};
         edt(() -> {
            app.getMainPanel().getPlate().putClientProperty(ClipboardOverride.CLIENT_PROPERTY, new ClipboardOverride() {
               @Override public void copy() { copies[0]++; }
            });
            app.getMainPanel().requestFocus();
            return null;
         });
         await(() -> app.getMainPanel().getPlate().isFocusOwner(), "canvas focused before Copy search");
         searchShortcut();
         query("copy");
         key(KeyEvent.VK_ENTER);
         key(KeyEvent.VK_ENTER);
         await(() -> copies[0] == 1, "real Copy Action dispatches to restored canvas focus");
         edt(() -> { app.getMainPanel().getPlate().putClientProperty(ClipboardOverride.CLIENT_PROPERTY, null); return null; });

         // Dynamic command labels retain their ID in history.
         edt(() -> { app.addSecondaryLayerAndActivate(); return null; });
         searchShortcut();
         query("hide layer");
         key(KeyEvent.VK_ENTER);
         key(KeyEvent.VK_ENTER);
         await(() -> !app.getMainPanel().getDocument().isActiveLayerVisible(), "Hide Layer search invocation");
         searchShortcut();
         await(() -> field.isFocusOwner(), "dynamic-label history");
         check(edt(() -> ((MenuEntry)list().getModel().getElementAt(0)).command().label().equals("Show Layer")), "history resolves new label for same command");
         key(KeyEvent.VK_ESCAPE);

         Path renamed = OUTPUT.resolve("window-renamed.txt").toAbsolutePath();
         Files.writeString(renamed, "probe");
         edt(() -> { app.getMainPanel().getDocument().setFile(renamed.toFile()); app.updateFrameTitle(); return null; });
         searchShortcut();
         query("window-renamed");
         check(edt(() -> selectableCount() == 1 && ((MenuEntry)list().getModel().getElementAt(0)).command().persistent()),
            "saved window receives current basename and persistent target");
         key(KeyEvent.VK_ESCAPE);

         // Deactivation also dismisses search while the results window owns focus.
         searchShortcut();
         query("grid");
         key(KeyEvent.VK_ENTER);
         await(() -> list().isFocusOwner(), "result focused before window deactivation");
         JFrame other = edt(() -> {
            JFrame next = new JFrame("Menu probe focus target");
            next.add(new JTextField("Focus target"));
            next.setBounds(60, 60, 200, 100);
            next.setVisible(true);
            next.toFront();
            return next;
         });
         await(() -> !list().isShowing() && field.getText().isEmpty(), "deactivation dismisses focused results");
         edt(() -> { other.dispose(); frame.toFront(); app.getMainPanel().requestFocus(); return null; });
         await(() -> app.getMainPanel().getPlate().isFocusOwner(), "canvas focus restored after deactivation");

         // The minimum width keeps all menu headings and the inline field accessible.
         edt(() -> { frame.setSize(frame.getMinimumSize().width, 600); return null; });
         robot.waitForIdle();
         check(edt(() -> {
            for (Component component : frame.getJMenuBar().getComponents()) {
               if (component instanceof JMenu && component.getX() + component.getWidth() > field.getX()) return false;
            }
            return field.getWidth() >= field.getMinimumSize().width;
         }), "narrow window keeps menu headings clear of search field");
         searchShortcut();
         query("grid");
         check(edt(() -> {
            Rectangle bounds = SwingUtilities.getWindowAncestor(list()).getBounds();
            return bounds.width == 320 && bounds.x + bounds.width == field.getLocationOnScreen().x + field.getWidth();
         }), "results remain 320 pixels wide and right aligned");
         capture("build/menu-search-narrow.png");
         Point canvas = edt(() -> app.getMainPanel().getPlate().getLocationOnScreen());
         robot.mouseMove(canvas.x + 10, canvas.y + 10);
         robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
         robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
         robot.waitForIdle();
         await(() -> !list().isShowing() && field.getText().isEmpty(), "outside click dismisses search");
         JMenu view = edt(() -> (JMenu)find(frame.getJMenuBar(), component -> component instanceof JMenu menu && menu.getText().equals("View")));
         Point menuPoint = edt(() -> view.getLocationOnScreen());
         robot.mouseMove(menuPoint.x + 10, menuPoint.y + 10);
         robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
         robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
         robot.waitForIdle();
         await(() -> view.getPopupMenu().isShowing(), "ordinary menus still open after previews");
         key(KeyEvent.VK_ESCAPE);

         Point fieldPoint = edt(() -> field.getLocationOnScreen());
         robot.mouseMove(fieldPoint.x + 10, fieldPoint.y + 10);
         robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
         robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
         robot.waitForIdle();
         await(() -> field.isFocusOwner() && list().isShowing(), "mouse click opens search field");
         key(KeyEvent.VK_ESCAPE);
         robot.mouseMove(40, 60);

         edt(() -> { app.getMainPanel().getDocument().setModified(false); app.doClose(frame); return null; });
         await(() -> app.getMainPanel().getEditorCount() == 0, "last probe document closed");
         searchShortcut();
         query("new");
         check(edt(() -> ((MenuEntry)list().getModel().getElementAt(0)).command().id().equals("file.newDocument")), "New command available without document");
         key(KeyEvent.VK_ENTER);
         await(() -> list().isFocusOwner(), "New result focused");
         key(KeyEvent.VK_ENTER);
         await(() -> app.getMainPanel().getEditorCount() == 1 && app.getMainPanel().getPlate().isFocusOwner(), "search executes with no previous document focus");
         System.out.println("PASS: actual JavE menu search keyboard, mouse, previews, dispatch, history, dynamic labels, clipboard focus, and narrow layout");
      } finally {
         edt(() -> {
            for (Window window : Window.getWindows()) {
               window.dispose();
            }
            return null;
         });
      }
   }

   private static void inventory(JMenu menu, String parent, List<String> values) {
      String path = parent + "/" + menu.getText();
      values.add("MENU " + path + " enabled=" + menu.isEnabled());
      for (Component component : menu.getMenuComponents()) {
         if (component instanceof JMenu child) {
            inventory(child, path, values);
         } else if (component instanceof JMenuItem item) {
            values.add(path + "/" + item.getText() + " enabled=" + item.isEnabled() + " selected="
               + (item instanceof JCheckBoxMenuItem check && check.isSelected()) + " shortcut=" + item.getAccelerator());
         } else {
            values.add(path + "/---");
         }
      }
   }

   private static Component find(Container container, java.util.function.Predicate<Component> predicate) {
      for (Component component : container.getComponents()) {
         if (predicate.test(component)) {
            return component;
         }
         if (component instanceof Container child) {
            Component found = find(child, predicate);
            if (found != null) {
               return found;
            }
         }
      }
      return null;
   }

   private static JList<?> list() {
      for (Window window : Window.getWindows()) {
         Component found = find(window, component -> component instanceof JList<?>
            && "Menu search results".equals(component.getAccessibleContext().getAccessibleName()));
         if (found != null) {
            return (JList<?>)found;
         }
      }
      return null;
   }

   private static int selectableCount() {
      int count = 0;
      for (int i = 0; i < list().getModel().getSize(); i++) {
         if (list().getModel().getElementAt(i) instanceof MenuEntry) {
            count++;
         }
      }
      return count;
   }

   private static void query(String query) throws Exception {
      edt(() -> { field.requestFocus(); field.setText(query); return null; });
      robot.waitForIdle();
      await(() -> field.isFocusOwner(), "query field focused");
   }

   private static void searchShortcut() {
      int modifier = (Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() & InputEvent.META_DOWN_MASK) != 0
         ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;
      chord(modifier, KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH);
   }

   private static void chord(int... keys) {
      for (int key : keys) robot.keyPress(key);
      for (int i = keys.length - 1; i >= 0; i--) robot.keyRelease(keys[i]);
      robot.waitForIdle();
   }

   private static void key(int code) throws Exception {
      chord(code);
      if (code == KeyEvent.VK_ESCAPE) {
         await(() -> app.getMainPanel().getPlate().isFocusOwner(), "Escape restores canvas focus");
      }
   }

   private static void capture(String path) throws Exception {
      robot.waitForIdle();
      Rectangle bounds = edt(() -> frame.getBounds());
      Files.createDirectories(OUTPUT);
      ImageIO.write(robot.createScreenCapture(bounds), "png", OUTPUT.resolve(Path.of(path).getFileName()).toFile());
   }

   private static void await(Supplier<Boolean> condition, String message) throws Exception {
      long deadline = System.nanoTime() + 3_000_000_000L;
      while (!edt(condition) && System.nanoTime() < deadline) {
         Thread.sleep(25);
      }
      if (!edt(condition)) {
         if (robot != null) capture("build/menu-search-probe-failure.png");
         throw new AssertionError(message + "; focus=" + edt(() -> KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner()));
      }
      System.out.println("OK " + message);
   }

   private static void check(boolean condition, String message) {
      if (!condition) throw new AssertionError(message);
      System.out.println("OK " + message);
   }

   private static <T> T edt(Supplier<T> action) throws Exception {
      AtomicReference<T> value = new AtomicReference<>();
      SwingUtilities.invokeAndWait(() -> value.set(action.get()));
      return value.get();
   }
}
