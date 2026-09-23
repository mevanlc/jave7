package de.jave.jave.tab;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.jave.jave.JavEApplication;
import de.jave.jave.PlateDocument;
import de.jave.jave.application.startup.ConfigurationList;
import de.jave.jave.menu.ProbePreferencesFactory;
import de.jave.jave.plate.JaveMainPanel;
import net.dizzy.commons.swing.dialog.tabbed.SmartTabbedPane;

import java.awt.Component;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.junit.BeforeClass;
import org.junit.Test;

public class DocumentTabIntegrationTest {

   @BeforeClass
   public static void setup() {
      System.setProperty("java.util.prefs.PreferencesFactory", ProbePreferencesFactory.class.getName());
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception ignored) {}
   }

   private static JavEApplication createApplication() throws Exception {
      AtomicReference<JavEApplication> ref = new AtomicReference<>();
      SwingUtilities.invokeAndWait(() -> {
         try {
            var init = Class.forName("de.jave.jave.application.startup.JaveStartup").getDeclaredMethod("initConfigFiles");
            init.setAccessible(true);
            JavEApplication app = new JavEApplication((ConfigurationList) init.invoke(null));
            app.startupMenuBar();
            ref.set(app);
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      });
      return ref.get();
   }

   private static SmartTabbedPane getTabbedPane(JaveMainPanel mainPanel) throws Exception {
      Field field = JaveMainPanel.class.getDeclaredField("tabbedPane");
      field.setAccessible(true);
      return (SmartTabbedPane) field.get(mainPanel);
   }

   private static List<JToggleButton> getTabButtons(SmartTabbedPane tabbedPane) {
      List<JToggleButton> buttons = new ArrayList<>();
      JPanel tabBar = (JPanel) tabbedPane.getContent().getComponent(0);
      for (Component comp : tabBar.getComponents()) {
         if (comp instanceof JToggleButton btn) {
            buttons.add(btn);
         }
      }
      return buttons;
   }

   @Test
   public void clickingTabSyncsDocumentManagerAndMainPanel() throws Exception {
      JavEApplication app = createApplication();
      SwingUtilities.invokeAndWait(() -> {
         try {
            app.doNew(); // Doc 1 (index 0)
            app.doNew(); // Doc 2 (index 1)
            app.doNew(); // Doc 3 (index 2)

            SmartTabbedPane pane = getTabbedPane(app.getMainPanel());
            assertEquals(2, app.getDocumentManager().getCurrentDocumentIndex());
            assertEquals(2, pane.getSelectedTabIndex());

            List<JToggleButton> buttons = getTabButtons(pane);
            assertEquals(3, buttons.size());
            assertFalse(buttons.get(0).isSelected());
            assertFalse(buttons.get(1).isSelected());
            assertTrue(buttons.get(2).isSelected());

            // Click active tab button (Doc 3): must remain selected
            buttons.get(2).doClick();
            assertEquals(2, app.getDocumentManager().getCurrentDocumentIndex());
            assertEquals(2, pane.getSelectedTabIndex());
            assertTrue("Active tab must remain selected", buttons.get(2).isSelected());

            // Click inactive tab button (Doc 1, index 0)
            buttons.get(0).doClick();
            assertEquals("DocumentManager must sync with clicked tab", 0, app.getDocumentManager().getCurrentDocumentIndex());
            assertEquals(0, pane.getSelectedTabIndex());
            assertTrue(buttons.get(0).isSelected());
            assertFalse(buttons.get(1).isSelected());
            assertFalse(buttons.get(2).isSelected());

            // Click active tab button (Doc 1) again: must remain selected
            buttons.get(0).doClick();
            assertEquals(0, app.getDocumentManager().getCurrentDocumentIndex());
            assertEquals(0, pane.getSelectedTabIndex());
            assertTrue(buttons.get(0).isSelected());

            // Switch to Doc 2 via app.setCurrentDocument
            app.setCurrentDocument(1);
            assertEquals(1, app.getDocumentManager().getCurrentDocumentIndex());
            assertEquals(1, pane.getSelectedTabIndex());
            assertFalse(buttons.get(0).isSelected());
            assertTrue(buttons.get(1).isSelected());
            assertFalse(buttons.get(2).isSelected());
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      });
   }

   @Test
   public void closingTabsMaintainsSyncAndCorrectSelection() throws Exception {
      JavEApplication app = createApplication();
      SwingUtilities.invokeAndWait(() -> {
         try {
            app.doNew(); // Doc 1 (index 0)
            app.doNew(); // Doc 2 (index 1)
            app.doNew(); // Doc 3 (index 2)

            SmartTabbedPane pane = getTabbedPane(app.getMainPanel());
            List<JToggleButton> buttons = getTabButtons(pane);

            // Switch to Doc 1 by clicking tab
            buttons.get(0).doClick();
            assertEquals(0, app.getDocumentManager().getCurrentDocumentIndex());
            assertEquals(0, pane.getSelectedTabIndex());

            // Close Doc 1
            app.doClose(null);
            assertEquals(app.getDocumentManager().getCurrentDocumentIndex(), pane.getSelectedTabIndex());

            buttons = getTabButtons(pane);
            assertEquals(2, buttons.size());
            int currentIndex = pane.getSelectedTabIndex();
            assertTrue(buttons.get(currentIndex).isSelected());

            // Close remaining tabs
            app.doClose(null);
            app.doClose(null);
            assertEquals(-1, app.getDocumentManager().getCurrentDocumentIndex());
            assertEquals(-1, pane.getSelectedTabIndex());
            assertEquals(0, getTabButtons(pane).size());

            // Create new tab when empty
            app.doNew();
            assertEquals(0, app.getDocumentManager().getCurrentDocumentIndex());
            assertEquals(0, pane.getSelectedTabIndex());
            buttons = getTabButtons(pane);
            assertEquals(1, buttons.size());
            assertTrue(buttons.get(0).isSelected());
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      });
   }
}
