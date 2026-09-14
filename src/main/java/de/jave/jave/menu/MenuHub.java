package de.jave.jave.menu;

import de.jave.jave.actions.SmartMenu;
import java.awt.Component;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import net.dizzy.commons.swing.action.SmartAction;

/**
 * Builds an ordered menu tree with a command at every executable leaf. The tree is also the
 * search catalog, so dynamic sections and later menu additions need no separate search registry.
 */
public final class MenuHub {
   private static final String COMMAND = "jave.menu.command";
   private static final String MENU_ID = "jave.menu.id";
   private static final String PLACEHOLDER = "jave.menu.placeholder";
   private final JMenuBar bar;
   private final List<Runnable> listeners = new ArrayList<>();
   private Runnable refreshState = () -> {};
   private Consumer<JMenuItem> decorate = item -> {};
   private boolean changePending;
   private final PropertyChangeListener actionChanges = event -> changed();

   public MenuHub(JMenuBar bar) {
      this.bar = bar;
   }

   public void setDecorator(Consumer<JMenuItem> decorator) {
      this.decorate = decorator;
   }

   public JMenu menu(String id, String label) {
      JMenu menu = new SmartMenu(label);
      menu.putClientProperty(MENU_ID, id);
      menu.getPopupMenu().addContainerListener(new ContainerAdapter() {
         @Override
         public void componentAdded(ContainerEvent event) {
            if (event.getChild() instanceof JMenuItem item) {
               decorate.accept(item);
            }
            changed();
         }

         @Override
         public void componentRemoved(ContainerEvent event) {
            changed();
         }
      });
      menu.addPropertyChangeListener(event -> {
         if ("enabled".equals(event.getPropertyName()) || "text".equals(event.getPropertyName())) {
            changed();
         }
      });
      return menu;
   }

   public void addMenu(JMenu menu) {
      bar.add(menu);
      changed();
   }

   public JMenuItem item(String id, Action action) {
      return bind(new JMenuItem(action), new MenuCommand(id, action));
   }

   public JCheckBoxMenuItem toggle(String id, Action action) {
      return toggle(id, action, true);
   }

   public JCheckBoxMenuItem toggle(String id, Action action, boolean persistent) {
      if (action.getValue(Action.SELECTED_KEY) == null) {
         action.putValue(Action.SELECTED_KEY, false);
      }
      return bind(new JCheckBoxMenuItem(action), new MenuCommand(id, action, persistent));
   }

   private <T extends JMenuItem> T bind(T item, MenuCommand command) {
      item.putClientProperty(COMMAND, command);
      command.action().addPropertyChangeListener(actionChanges);
      decorate.accept(item);
      return item;
   }

   public JMenuItem placeholder(String text) {
      JMenuItem item = new JMenuItem(text);
      item.setEnabled(false);
      item.putClientProperty(PLACEHOLDER, true);
      decorate.accept(item);
      return item;
   }

   public static Action action(String label, Consumer<Component> execute) {
      Action action = new SmartAction() {
         @Override
         protected void execute(Component parent) {
            execute.accept(parent);
         }
      };
      // Dynamic labels may be filenames containing '&', not mnemonic markup.
      action.putValue(Action.NAME, label);
      return action;
   }

   public static Action toggleAction(String label, Consumer<Boolean> execute) {
      Action action = new SmartAction(label) {
         @Override
         protected void execute(Component source) {
            boolean selected = source instanceof javax.swing.AbstractButton button
               ? button.isSelected() : !Boolean.TRUE.equals(getValue(Action.SELECTED_KEY));
            putValue(Action.SELECTED_KEY, selected);
            execute.accept(selected);
         }
      };
      action.putValue(Action.SELECTED_KEY, false);
      return action;
   }

   public void setStateRefresher(Runnable refreshState) {
      this.refreshState = refreshState;
   }

   public void refreshState() {
      refreshState.run();
   }

   public List<MenuEntry> entries() {
      Map<String, MenuEntry> entries = new LinkedHashMap<>();
      for (Component component : bar.getComponents()) {
         if (component instanceof JMenu menu) {
            collect(menu, new ArrayList<>(), entries);
         }
      }
      return List.copyOf(entries.values());
   }

   private void collect(JMenu menu, List<JMenu> parents, Map<String, MenuEntry> entries) {
      if (menu.getClientProperty(MENU_ID) == null) {
         throw new IllegalStateException("Menu was not created through the menu hub: " + menu.getText());
      }
      if (!menu.isVisible()) {
         return;
      }
      List<JMenu> path = new ArrayList<>(parents);
      path.add(menu);
      for (Component component : menu.getMenuComponents()) {
         if (component instanceof JMenu child) {
            collect(child, path, entries);
         } else if (component instanceof JMenuItem item && item.isVisible()
            && !Boolean.TRUE.equals(item.getClientProperty(PLACEHOLDER))) {
            MenuCommand command = (MenuCommand)item.getClientProperty(COMMAND);
            if (command == null || command.action() != item.getAction()) {
               throw new IllegalStateException("Menu item has no registered command: " + item.getText());
            }
            if (entries.put(command.id(), new MenuEntry(command, item, path)) != null) {
               throw new IllegalStateException("Duplicate menu command ID: " + command.id());
            }
         }
      }
   }

   public void addChangeListener(Runnable listener) {
      listeners.add(listener);
   }

   public void removeChangeListener(Runnable listener) {
      listeners.remove(listener);
   }

   private void changed() {
      if (!changePending) {
         changePending = true;
         SwingUtilities.invokeLater(() -> {
            changePending = false;
            List.copyOf(listeners).forEach(Runnable::run);
         });
      }
   }

   public MenuSearchController install(JFrame frame, MenuSearchHistory history, Runnable stateRefresher,
      java.util.function.Supplier<Component> fallbackFocus) {
      setStateRefresher(stateRefresher);
      entries(); // Reject incomplete registrations before installing the primary menu bar.
      frame.setJMenuBar(bar);
      return new MenuSearchController(frame, bar, this, history, fallbackFocus);
   }
}
