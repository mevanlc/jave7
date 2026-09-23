package de.jave.jave.tab;

import de.jave.jave.JavEApplication;
import de.jave.jave.application.startup.ConfigurationList;
import de.jave.jave.menu.ProbePreferencesFactory;
import de.jave.jave.plate.JaveMainPanel;
import net.dizzy.commons.swing.dialog.tabbed.SmartTabbedPane;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class DocumentTabUiProbe {
   private static JavEApplication app;
   private static JFrame frame;
   private static Robot robot;
   private static final Path OUTPUT = Path.of(System.getProperty("jave.probeOutput", "build/tab-ui-probe/output"));

   public static void main(String[] args) throws Exception {
      try {
         run();
         System.out.println("\nALL DOCUMENT TAB PROBE TESTS PASSED SUCCESSFULLY!");
         System.exit(0);
      } catch (Throwable error) {
         error.printStackTrace();
         System.exit(1);
      }
   }

   private static void run() throws Exception {
      System.setProperty("java.util.prefs.PreferencesFactory", ProbePreferencesFactory.class.getName());
      edt(() -> {
         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JPopupMenu.setDefaultLightWeightPopupEnabled(false);
            var init = Class.forName("de.jave.jave.application.startup.JaveStartup").getDeclaredMethod("initConfigFiles");
            init.setAccessible(true);
            app = new JavEApplication((ConfigurationList)init.invoke(null));
            app.startupMenuBar();
            app.doNew(); // Doc 1
            app.doNew(); // Doc 2
            app.doNew(); // Doc 3
            frame = app.getFrame();
            frame.setBounds(120, 100, 1100, 720);
            frame.setAlwaysOnTop(true);
            frame.setVisible(true);
            frame.toFront();
            frame.requestFocus();
            try {
               java.awt.Desktop.getDesktop().requestForeground(true);
            } catch (Throwable ignored) {}
            app.getMainPanel().requestFocus();
            return null;
         } catch (Exception exception) {
            throw new RuntimeException(exception);
         }
      });

      robot = new Robot();
      robot.setAutoDelay(50);
      robot.waitForIdle();
      Thread.sleep(500);

      Files.createDirectories(OUTPUT);

      // Phase 1: Verify Initial State with 3 documents
      System.out.println("\n=== PHASE 1: Initial State (3 docs created) ===");
      printState("INITIAL STATE (3 docs created)");
      capture("tabs-1-initial.png");
      assertState(2, 2, 3);
      assertSelectedButton(2);

      // Phase 2: Click currently active tab (Doc 3, index 2)
      System.out.println("\n=== PHASE 2: Click currently active tab (Doc 3) ===");
      List<JToggleButton> tabButtons = edt(() -> getTabButtons());
      clickComponent(tabButtons.get(2));
      robot.waitForIdle();
      Thread.sleep(200);
      printState("AFTER CLICKING ACTIVE TAB (Doc 3)");
      capture("tabs-2-click-active-stays-selected.png");
      assertState(2, 2, 3);
      assertSelectedButton(2); // MUST REMAIN SELECTED!

      // Phase 3: Click inactive tab (Doc 1, index 0)
      System.out.println("\n=== PHASE 3: Click inactive tab (Doc 1) ===");
      tabButtons = edt(() -> getTabButtons());
      clickComponent(tabButtons.get(0));
      robot.waitForIdle();
      Thread.sleep(200);
      printState("AFTER CLICKING INACTIVE TAB (Doc 1)");
      capture("tabs-3-click-inactive-doc1.png");
      assertState(0, 0, 3); // DocumentManager MUST SYNC with TabbedPane!
      assertSelectedButton(0);

      // Phase 4: Click Doc 1 (now active) again
      System.out.println("\n=== PHASE 4: Click currently active tab (Doc 1) again ===");
      tabButtons = edt(() -> getTabButtons());
      clickComponent(tabButtons.get(0));
      robot.waitForIdle();
      Thread.sleep(200);
      printState("AFTER CLICKING ACTIVE TAB (Doc 1) AGAIN");
      capture("tabs-4-click-doc1-again.png");
      assertState(0, 0, 3);
      assertSelectedButton(0); // MUST REMAIN SELECTED!

      // Phase 5: Switch via doNextDocument()
      System.out.println("\n=== PHASE 5: Switch via doNextDocument() ===");
      edt(() -> {
         app.doNextDocument();
         return null;
      });
      robot.waitForIdle();
      Thread.sleep(200);
      printState("AFTER doNextDocument()");
      capture("tabs-5-next-document.png");
      assertState(1, 1, 3);
      assertSelectedButton(1);

      // Phase 6: Create a new tab (Doc 4)
      System.out.println("\n=== PHASE 6: Create new tab (Doc 4) ===");
      edt(() -> {
         app.doNew();
         return null;
      });
      robot.waitForIdle();
      Thread.sleep(200);
      printState("AFTER CREATING NEW TAB (Doc 4)");
      capture("tabs-6-create-new-tab.png");
      assertState(3, 3, 4);
      assertSelectedButton(3);

      // Phase 7: Close active tab (Doc 4)
      System.out.println("\n=== PHASE 7: Close active tab (Doc 4) ===");
      edt(() -> {
         app.doClose(frame);
         return null;
      });
      robot.waitForIdle();
      Thread.sleep(200);
      printState("AFTER CLOSING ACTIVE TAB (Doc 4)");
      capture("tabs-7-close-active-tab.png");
      assertState(2, 2, 3);
      assertSelectedButton(2);

      // Phase 8: Switch to middle tab (Doc 2, index 1) and close it
      System.out.println("\n=== PHASE 8: Switch to middle tab (Doc 2) and close it ===");
      tabButtons = edt(() -> getTabButtons());
      clickComponent(tabButtons.get(1));
      robot.waitForIdle();
      Thread.sleep(200);
      assertState(1, 1, 3);
      assertSelectedButton(1);

      edt(() -> {
         app.doClose(frame);
         return null;
      });
      robot.waitForIdle();
      Thread.sleep(200);
      printState("AFTER CLOSING MIDDLE TAB");
      capture("tabs-8-close-middle-tab.png");
      // Remaining 2 tabs, index should be in sync and one tab selected
      int currentDoc = edt(() -> app.getDocumentManager().getCurrentDocumentIndex());
      int currentPane = edt(() -> getSmartTabbedPane().getSelectedTabIndex());
      if (currentDoc != currentPane) {
         throw new AssertionError("DocMgr (" + currentDoc + ") != Pane (" + currentPane + ")");
      }
      assertSelectedButton(currentPane);

      // Phase 9: Close remaining tabs until 0 tabs
      System.out.println("\n=== PHASE 9: Close remaining tabs until empty ===");
      edt(() -> {
         app.doClose(frame);
         return null;
      });
      robot.waitForIdle();
      Thread.sleep(100);
      edt(() -> {
         app.doClose(frame);
         return null;
      });
      robot.waitForIdle();
      Thread.sleep(200);
      printState("AFTER CLOSING ALL TABS");
      capture("tabs-9-all-tabs-closed.png");
      assertState(-1, -1, 0);

      // Phase 10: Create new tab from empty state
      System.out.println("\n=== PHASE 10: Create new tab from empty state ===");
      edt(() -> {
         app.doNew();
         return null;
      });
      robot.waitForIdle();
      Thread.sleep(200);
      printState("AFTER CREATING TAB FROM EMPTY");
      capture("tabs-10-new-from-empty.png");
      assertState(0, 0, 1);
      assertSelectedButton(0);
   }

   private static void assertState(int expectedDocMgrIndex, int expectedPaneIndex, int expectedTabCount) throws Exception {
      edt(() -> {
         int docMgrIdx = app.getDocumentManager().getCurrentDocumentIndex();
         int paneIdx = getSmartTabbedPane().getSelectedTabIndex();
         int tabCount = getTabButtons().size();
         if (docMgrIdx != expectedDocMgrIndex) {
            throw new AssertionError("DocumentManager index expected " + expectedDocMgrIndex + " but was " + docMgrIdx);
         }
         if (paneIdx != expectedPaneIndex) {
            throw new AssertionError("SmartTabbedPane index expected " + expectedPaneIndex + " but was " + paneIdx);
         }
         if (tabCount != expectedTabCount) {
            throw new AssertionError("Tab count expected " + expectedTabCount + " but was " + tabCount);
         }
         return null;
      });
   }

   private static void assertSelectedButton(int expectedSelectedIndex) throws Exception {
      edt(() -> {
         List<JToggleButton> buttons = getTabButtons();
         for (int i = 0; i < buttons.size(); i++) {
            boolean shouldBeSelected = (i == expectedSelectedIndex);
            boolean isSelected = buttons.get(i).isSelected();
            if (isSelected != shouldBeSelected) {
               throw new AssertionError("Button " + i + " ('" + buttons.get(i).getText()
                  + "') expected isSelected=" + shouldBeSelected + " but was " + isSelected);
            }
         }
         return null;
      });
   }

   private static List<JToggleButton> getTabButtons() {
      List<JToggleButton> buttons = new ArrayList<>();
      SmartTabbedPane tabbedPane = getSmartTabbedPane();
      if (tabbedPane != null) {
         JPanel tabBar = (JPanel) tabbedPane.getContent().getComponent(0);
         for (Component comp : tabBar.getComponents()) {
            if (comp instanceof JToggleButton btn) {
               buttons.add(btn);
            }
         }
      }
      return buttons;
   }

   private static SmartTabbedPane getSmartTabbedPane() {
      JaveMainPanel mainPanel = app.getMainPanel();
      try {
         var field = JaveMainPanel.class.getDeclaredField("tabbedPane");
         field.setAccessible(true);
         return (SmartTabbedPane) field.get(mainPanel);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   private static void printState(String label) throws Exception {
      edt(() -> {
         System.out.println("=== " + label + " ===");
         int docMgrIdx = app.getDocumentManager().getCurrentDocumentIndex();
         SmartTabbedPane pane = getSmartTabbedPane();
         int paneIdx = pane.getSelectedTabIndex();
         System.out.println("DocumentManager index: " + docMgrIdx);
         System.out.println("SmartTabbedPane index: " + paneIdx);
         List<JToggleButton> buttons = getTabButtons();
         for (int i = 0; i < buttons.size(); i++) {
            JToggleButton b = buttons.get(i);
            System.out.println("  Button " + i + " ('" + b.getText() + "'): isSelected=" + b.isSelected() + ", bg=" + b.getBackground());
         }
         return null;
      });
   }

   private static void clickComponent(Component c) throws Exception {
      Point p = edt(() -> {
         Point loc = c.getLocationOnScreen();
         loc.translate(c.getWidth() / 2, c.getHeight() / 2);
         return loc;
      });
      robot.mouseMove(p.x, p.y);
      robot.waitForIdle();
      robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
      robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
      robot.waitForIdle();
   }

   private static void capture(String fileName) throws Exception {
      robot.waitForIdle();
      Rectangle bounds = edt(() -> {
         SmartTabbedPane pane = getSmartTabbedPane();
         JPanel tabBar = (JPanel) pane.getContent().getComponent(0);
         Point loc = tabBar.getLocationOnScreen();
         return new Rectangle(loc.x, loc.y - 10, frame.getWidth() - 20, Math.max(tabBar.getHeight() + 40, 50));
      });
      BufferedImage capture = robot.createScreenCapture(bounds);
      Path dest = OUTPUT.resolve(fileName);
      ImageIO.write(capture, "png", dest.toFile());
      System.out.println("Saved capture: " + dest);
   }

   private static <T> T edt(Supplier<T> action) throws Exception {
      AtomicReference<T> value = new AtomicReference<>();
      SwingUtilities.invokeAndWait(() -> value.set(action.get()));
      return value.get();
   }
}
