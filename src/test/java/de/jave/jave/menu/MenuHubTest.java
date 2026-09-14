package de.jave.jave.menu;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.junit.Assert;
import org.junit.Test;

public class MenuHubTest {
   @Test
   public void menuTreeIsTheOrderedCatalogIncludingDisabledAncestors() throws Exception {
      SwingUtilities.invokeAndWait(() -> {
         JMenuBar bar = new JMenuBar();
         MenuHub hub = new MenuHub(bar);
         JMenu edit = hub.menu("edit", "&Edit");
         JMenu nested = hub.menu("nested", "Transform");
         edit.add(hub.item("copy", MenuHub.action("Copy", parent -> {})));
         edit.addSeparator();
         edit.add(nested);
         nested.add(hub.item("rotate", MenuHub.action("Rotate", parent -> {})));
         nested.add(hub.placeholder("none"));
         hub.addMenu(edit);
         bar.add(Box.createHorizontalGlue());
         bar.add(new JTextField());
         List<MenuEntry> entries = hub.entries();
         Assert.assertEquals(List.of("copy", "rotate"), entries.stream().map(entry -> entry.command().id()).toList());
         Assert.assertEquals("Edit > Transform > Rotate", entries.get(1).location());
         Assert.assertTrue(entries.get(1).isEnabled());
         edit.setEnabled(false);
         Assert.assertFalse(entries.get(1).isEnabled());
      });
   }

   @Test
   public void changingActionPropertiesUpdatesBothCatalogAndWidgetWithoutChangingIdentity() throws Exception {
      SwingUtilities.invokeAndWait(() -> {
         MenuHub hub = new MenuHub(new JMenuBar());
         JMenu menu = hub.menu("edit", "Edit");
         Action undo = MenuHub.action("Undo", parent -> {});
         JMenuItem item = hub.item("edit.undo", undo);
         menu.add(item);
         hub.addMenu(menu);
         MenuEntry entry = hub.entries().get(0);
         undo.putValue(Action.NAME, "Undo draw rectangle");
         undo.setEnabled(false);
         Assert.assertEquals("edit.undo", entry.command().id());
         Assert.assertEquals("Undo draw rectangle", entry.command().label());
         Assert.assertEquals(entry.command().label(), item.getText());
         Assert.assertFalse(entry.isEnabled());
      });
   }

   @Test
   public void dynamicTargetLabelsPreserveLiteralAmpersands() {
      Action action = MenuHub.action("1 notes & sketches.txt", parent -> {});
      JMenuItem item = new MenuHub(new JMenuBar()).item("window.notes", action);
      Assert.assertEquals("1 notes & sketches.txt", item.getText());
      Assert.assertEquals(item.getText(), new MenuCommand("window.notes", action).label());
   }

   @Test
   public void replacingDynamicItemsResolvesTheNewTargetInsteadOfAnOldIndex() throws Exception {
      SwingUtilities.invokeAndWait(() -> {
         MenuHub hub = new MenuHub(new JMenuBar());
         JMenu recent = hub.menu("recent", "Recent Files");
         hub.addMenu(recent);
         AtomicInteger target = new AtomicInteger();
         recent.add(hub.item("recent.file", MenuHub.action("0 file", parent -> target.set(1))));
         recent.removeAll();
         recent.add(hub.item("recent.file", MenuHub.action("1 file", parent -> target.set(2))));
         Assert.assertEquals(1, hub.entries().size());
         hub.entries().get(0).item().doClick(0);
         Assert.assertEquals(2, target.get());
      });
   }

   @Test
   public void togglesExecuteOnceAndShareSelectedStateWithTheirAction() throws Exception {
      SwingUtilities.invokeAndWait(() -> {
         MenuHub hub = new MenuHub(new JMenuBar());
         AtomicBoolean selected = new AtomicBoolean();
         AtomicInteger calls = new AtomicInteger();
         Action action = MenuHub.toggleAction("Grid", value -> {
            selected.set(value);
            calls.incrementAndGet();
         });
         JCheckBoxMenuItem item = hub.toggle("grid", action);
         item.doClick(0);
         Assert.assertTrue(selected.get());
         Assert.assertEquals(1, calls.get());
         Assert.assertEquals(true, action.getValue(Action.SELECTED_KEY));
         item.doClick(0);
         Assert.assertFalse(selected.get());
         Assert.assertEquals(2, calls.get());
         action.putValue(Action.SELECTED_KEY, true);
         Assert.assertTrue(item.isSelected());
      });
   }

   @Test
   public void modelChangesUpdateTheSharedMenuAndToolbarToggle() throws Exception {
      SwingUtilities.invokeAndWait(() -> {
         var model = new net.dizzy.commons.core.model.BooleanModel(false);
         var action = new net.dizzy.commons.swing.action.SmartToggleAction(model, "Grid");
         var toolbar = new javax.swing.JToggleButton(action);
         var menu = new MenuHub(new JMenuBar()).toggle("grid", action);
         model.setValue(true);
         Assert.assertTrue(menu.isSelected());
         Assert.assertTrue(toolbar.isSelected());
         menu.doClick(0);
         Assert.assertFalse(model.getValue());
         Assert.assertFalse(toolbar.isSelected());
      });
   }

   @Test
   public void groupedChoicesKeepActionSelectionInSync() throws Exception {
      SwingUtilities.invokeAndWait(() -> {
         MenuHub hub = new MenuHub(new JMenuBar());
         JCheckBoxMenuItem first = hub.toggle("first", MenuHub.action("First", parent -> {}));
         JCheckBoxMenuItem second = hub.toggle("second", MenuHub.action("Second", parent -> {}));
         ButtonGroup group = new ButtonGroup();
         group.add(first);
         group.add(second);
         first.doClick(0);
         second.doClick(0);
         Assert.assertEquals(false, first.getAction().getValue(Action.SELECTED_KEY));
         Assert.assertEquals(true, second.getAction().getValue(Action.SELECTED_KEY));
      });
   }

   @Test
   public void hubRejectsUnregisteredExecutableItemsAndDuplicateIds() throws Exception {
      SwingUtilities.invokeAndWait(() -> {
         MenuHub hub = new MenuHub(new JMenuBar());
         JMenu menu = hub.menu("file", "File");
         hub.addMenu(menu);
         menu.add(new JMenuItem("Unregistered"));
         Assert.assertThrows(IllegalStateException.class, hub::entries);
         menu.removeAll();
         menu.add(hub.item("same", MenuHub.action("One", parent -> {})));
         menu.add(hub.item("same", MenuHub.action("Two", parent -> {})));
         Assert.assertThrows(IllegalStateException.class, hub::entries);
      });
   }
}
