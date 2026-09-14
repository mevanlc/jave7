package de.jave.jave.menu;

import de.jave.jave.actions.JaveKeyBindings;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultEditorKit;

/** Inline primary-menu search. The results window and menu previews have independent lifetimes. */
public final class MenuSearchController implements AutoCloseable {
   private final JFrame frame;
   private final JMenuBar bar;
   private final MenuHub hub;
   private final MenuSearchHistory history;
   private final Supplier<Component> fallbackFocus;
   private final JTextField field = new SearchField();
   private final DefaultListModel<Object> rows = new DefaultListModel<>();
   private final JList<Object> results = new JList<>(rows);
   private final JWindow window;
   private final MenuLocationPreview preview;
   private final Runnable catalogChanged = this::refreshResults;
   private final KeyEventDispatcher keys = this::dispatchKey;
   private final AWTEventListener outsideClicks = this::eventDispatched;
   private List<MenuEntry> visible = List.of();
   private int focusedIndex = -1;
   private boolean active;
   private boolean refreshing;
   private Component previousFocus;
   private int caretDot;
   private int caretMark;
   private boolean restoreCaretOnFocus;
   private final int rowHeight;
   private final WindowAdapter windowEvents;
   private final ComponentAdapter frameEvents;
   private final PropertyChangeListener focusChanges;
   private static final String FOCUS_ACTION = "jave.searchMenus";

   public MenuSearchController(JFrame frame, JMenuBar bar, MenuHub hub, MenuSearchHistory history,
      Supplier<Component> fallbackFocus) {
      this.frame = frame;
      this.bar = bar;
      this.hub = hub;
      this.history = history;
      this.fallbackFocus = fallbackFocus;
      window = new JWindow(frame);
      window.setType(Window.Type.POPUP);
      window.setAutoRequestFocus(false);
      window.setFocusableWindowState(true);
      window.getRootPane().setBorder(BorderFactory.createLineBorder(color("Separator.foreground", Color.GRAY)));
      preview = new MenuLocationPreview(frame);
      Font font = UIManager.getFont("MenuItem.font");
      results.setFont(font == null ? field.getFont() : font);
      rowHeight = results.getFontMetrics(results.getFont()).getHeight() + 8;
      results.setFixedCellHeight(rowHeight);
      results.setSelectionModel(new javax.swing.DefaultListSelectionModel() {
         @Override public void setSelectionInterval(int first, int last) {
            if (first >= 0 && last >= 0 && first < visible.size() && last < visible.size()) {
               super.setSelectionInterval(first, last);
            }
         }
      });
      results.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      results.setFocusTraversalKeysEnabled(false);
      results.setCellRenderer(new ResultRenderer());
      results.getAccessibleContext().setAccessibleName("Menu search results");
      window.add(results);
      field.setName("menuSearchField");
      field.getAccessibleContext().setAccessibleName("Search menus");
      field.setToolTipText("Search menus (Cmd/Ctrl+Shift+/)");
      field.setFocusTraversalKeysEnabled(false);
      bar.add(Box.createHorizontalGlue());
      bar.add(Box.createHorizontalStrut(8));
      bar.add(field);
      bar.add(Box.createHorizontalStrut(8));

      field.addFocusListener(new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent event) {
            // Hiding a focused results window briefly reactivates the field on macOS.
            // That native restoration must not reopen a dismissed search.
            if (event.getCause() != FocusEvent.Cause.ACTIVATION) {
               if (!active) {
                  begin(event.getOppositeComponent());
               } else if (focusedIndex >= 0) {
                  focusField(false);
               }
            }
            if (restoreCaretOnFocus) {
               restoreCaret();
               restoreCaretOnFocus = false;
            }
         }
      });
      field.addMouseListener(new MouseAdapter() {
         @Override public void mousePressed(MouseEvent event) {
            if (!active) {
               begin(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
            }
            restoreCaretOnFocus = false;
            focusField(false);
         }
      });
      field.getDocument().addDocumentListener(new DocumentListener() {
         @Override public void insertUpdate(DocumentEvent event) { queryChanged(); }
         @Override public void removeUpdate(DocumentEvent event) { queryChanged(); }
         @Override public void changedUpdate(DocumentEvent event) { queryChanged(); }
      });
      MouseAdapter mouse = new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent event) {
            mouseMoved(event);
         }

         @Override
         public void mouseMoved(MouseEvent event) {
            int index = resultAt(event.getPoint());
            if (index >= 0 && index != focusedIndex) {
               focusResult(index);
            }
         }

         @Override
         public void mouseClicked(MouseEvent event) {
            if (SwingUtilities.isLeftMouseButton(event)) {
               int index = resultAt(event.getPoint());
               if (index >= 0) {
                  focusResult(index);
                  activate();
               }
            }
         }
      };
      results.addMouseMotionListener(mouse);
      results.addMouseListener(mouse);
      frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(JaveKeyBindings.SEARCH_MENUS, FOCUS_ACTION);
      frame.getRootPane().getActionMap().put(FOCUS_ACTION, new AbstractAction() {
         @Override public void actionPerformed(ActionEvent event) { focusSearch(); }
      });
      hub.addChangeListener(catalogChanged);
      windowEvents = new WindowAdapter() {
         @Override public void windowDeactivated(WindowEvent event) {
            if (!isSearchWindow(event.getOppositeWindow())) {
               dismiss(false);
            }
         }
         @Override public void windowClosed(WindowEvent event) {
            if (event.getWindow() == frame) {
               close();
            }
         }
      };
      frame.addWindowListener(windowEvents);
      window.addWindowListener(windowEvents);
      frameEvents = new ComponentAdapter() {
         @Override public void componentMoved(ComponentEvent event) { refreshResults(); }
         @Override public void componentResized(ComponentEvent event) { refreshResults(); }
         @Override public void componentHidden(ComponentEvent event) { dismiss(false); }
      };
      frame.addComponentListener(frameEvents);
      focusChanges = event -> {
         if (active && event.getNewValue() instanceof Component focus
            && focus != field && !SwingUtilities.isDescendingFrom(focus, window)) {
            SwingUtilities.invokeLater(() -> {
               Component current = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
               if (active && current != null && current != field && !SwingUtilities.isDescendingFrom(current, window)) {
                  dismiss(false);
               }
            });
         }
      };
      KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner", focusChanges);
      // JMenuBar.getMenuCount includes glue/fields. Its preferred width still accounts for every menu.
      int minimumWidth = bar.getPreferredSize().width - field.getPreferredSize().width + field.getMinimumSize().width;
      Insets frameInsets = frame.getInsets();
      frame.setMinimumSize(new Dimension(minimumWidth + frameInsets.left + frameInsets.right,
         Math.max(frame.getMinimumSize().height, 160)));
   }

   public void focusSearch() {
      if (!active) {
         begin(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
      }
      focusField(true);
   }

   private void begin(Component origin) {
      previousFocus = origin != field && origin != null ? origin : fallbackFocus.get();
      active = true;
      focusedIndex = -1;
      MenuSelectionManager.defaultManager().clearSelectedPath();
      KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keys);
      Toolkit.getDefaultToolkit().addAWTEventListener(outsideClicks, AWTEvent.MOUSE_EVENT_MASK);
      hub.refreshState();
      refreshResults();
   }

   private void queryChanged() {
      if (active) {
         focusField(false);
         refreshResults();
      }
   }

   private void refreshResults() {
      if (!active || refreshing || !field.isShowing()) {
         return;
      }
      refreshing = true;
      try {
         String focusedId = focusedIndex >= 0 && focusedIndex < visible.size() ? visible.get(focusedIndex).command().id() : null;
         hub.refreshState();
         List<MenuEntry> matches = MenuSearchModel.match(hub.entries(), field.getText(), history.ids());
         Point origin = field.getLocationOnScreen();
         Rectangle screen = MenuLocationPreview.screenBounds(field);
         int y = origin.y + field.getHeight();
         int rowLimit = Math.max(1, (screen.y + screen.height - y - 2) / rowHeight);
         MenuSearchModel.Page page = MenuSearchModel.page(matches, rowLimit);
         visible = page.entries();
         rows.clear();
         visible.forEach(rows::addElement);
         if (page.omitted() > 0) {
            rows.addElement("… " + page.omitted() + " more results not shown …");
         } else if (rows.isEmpty()) {
            rows.addElement(field.getText().isBlank() ? "No menu search history" : "No matching menu items");
         }
         window.setBounds(origin.x + field.getWidth() - MenuSearchModel.RESULTS_WIDTH, y,
            MenuSearchModel.RESULTS_WIDTH, rows.size() * rowHeight + 2);
         window.setVisible(true);
         int next = -1;
         if (focusedId != null) {
            for (int i = 0; i < visible.size(); i++) {
               if (visible.get(i).command().id().equals(focusedId)) {
                  next = i;
                  break;
               }
            }
         }
         if (next >= 0) {
            focusResult(next);
         } else {
            focusField(focusedId != null);
         }
      } finally {
         refreshing = false;
      }
   }

   private int resultAt(Point point) {
      int index = results.locationToIndex(point);
      return index >= 0 && index < visible.size() && results.getCellBounds(index, index).contains(point) ? index : -1;
   }

   private void focusResult(int index) {
      if (!active || index < 0 || index >= visible.size()) {
         return;
      }
      if (focusedIndex < 0) {
         rememberCaret();
      }
      focusedIndex = index;
      results.setSelectedIndex(index);
      preview.show(visible.get(index), window.getBounds());
      // A native mouse-move event cannot transfer focus to another window on macOS.
      // Request it after that event, when the heavyweight preview popups also exist.
      SwingUtilities.invokeLater(() -> {
         if (active && focusedIndex == index) {
            results.requestFocus();
         }
      });
      results.getAccessibleContext().setAccessibleDescription(visible.get(index).location());
      results.repaint();
   }

   private void focusField(boolean requestFocus) {
      if (requestFocus && focusedIndex >= 0) {
         restoreCaretOnFocus = true;
         restoreCaret();
      }
      focusedIndex = -1;
      results.clearSelection();
      preview.hide();
      if (requestFocus) {
         field.requestFocus();
      }
      field.getCaret().setVisible(field.isFocusOwner());
      results.repaint();
   }

   private void rememberCaret() {
      caretDot = field.getCaret().getDot();
      caretMark = field.getCaret().getMark();
   }

   private void restoreCaret() {
      int length = field.getDocument().getLength();
      field.setCaretPosition(Math.min(caretMark, length));
      field.moveCaretPosition(Math.min(caretDot, length));
   }

   private boolean dispatchKey(KeyEvent event) {
      if (!active || !isSearchWindow(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow())) {
         return false;
      }
      int code = event.getKeyCode();
      boolean plain = !event.isControlDown() && !event.isMetaDown() && !event.isAltDown();
      boolean navigation = plain && (code == KeyEvent.VK_TAB || code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN
         || code == KeyEvent.VK_ENTER || code == KeyEvent.VK_ESCAPE);
      if (event.getID() != KeyEvent.KEY_PRESSED) {
         if (navigation || event.getID() == KeyEvent.KEY_TYPED && (event.getKeyChar() == '\n' || event.getKeyChar() == '\t')) {
            return true;
         }
         if (event.getID() == KeyEvent.KEY_TYPED && focusedIndex >= 0 && plain && !Character.isISOControl(event.getKeyChar())) {
            focusField(true);
            field.replaceSelection(String.valueOf(event.getKeyChar()));
            rememberCaret();
            return true;
         }
         return false;
      }
      if (JaveKeyBindings.SEARCH_MENUS.equals(KeyStroke.getKeyStrokeForEvent(event))) {
         focusField(true);
         return true;
      }
      int primary = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
      if ((event.getModifiersEx() & primary) != 0 && !event.isAltDown() && !event.isShiftDown()
         && (code == KeyEvent.VK_A || code == KeyEvent.VK_X || code == KeyEvent.VK_C || code == KeyEvent.VK_V)) {
         focusField(true);
         switch (code) {
            case KeyEvent.VK_A -> field.selectAll();
            case KeyEvent.VK_X -> field.cut();
            case KeyEvent.VK_C -> field.copy();
            case KeyEvent.VK_V -> field.paste();
            default -> { }
         }
         rememberCaret();
         return true;
      }
      if (plain && focusedIndex >= 0 && code == KeyEvent.VK_BACK_SPACE) {
         focusField(true);
         field.getActionMap().get(DefaultEditorKit.deletePrevCharAction).actionPerformed(new ActionEvent(field, ActionEvent.ACTION_PERFORMED, ""));
         rememberCaret();
         return true;
      }
      if (!navigation) {
         return false;
      }
      switch (code) {
         case KeyEvent.VK_ESCAPE -> dismiss(true);
         case KeyEvent.VK_ENTER -> {
            if (focusedIndex < 0) {
               focusResult(0);
            } else {
               activate();
            }
         }
         default -> {
            int direction = code == KeyEvent.VK_UP || code == KeyEvent.VK_TAB && event.isShiftDown() ? -1 : 1;
            int index = MenuSearchModel.nextFocus(focusedIndex, direction, visible.size());
            if (index < 0) {
               focusField(true);
            } else {
               focusResult(index);
            }
         }
      }
      return true;
   }

   private boolean isSearchWindow(Window candidate) {
      return candidate == frame || candidate == window;
   }

   private void eventDispatched(AWTEvent event) {
      if (active && event instanceof MouseEvent mouse && mouse.getID() == MouseEvent.MOUSE_PRESSED) {
         Component source = mouse.getComponent();
         if (source != field && !SwingUtilities.isDescendingFrom(source, window)) {
            dismiss(false);
         }
      }
   }

   private void activate() {
      if (!active || focusedIndex < 0 || focusedIndex >= visible.size()) {
         return;
      }
      String id = visible.get(focusedIndex).command().id();
      hub.refreshState();
      MenuEntry entry = resolve(id);
      if (entry == null || !entry.isEnabled()) {
         refreshResults();
         return;
      }
      Component target = restoreTarget();
      dismiss(false);
      afterFocus(target, () -> {
         hub.refreshState();
         MenuEntry current = resolve(id);
         if (current != null && current.isEnabled()) {
            history.record(current.command());
            current.item().doClick(0);
         }
      });
   }

   private MenuEntry resolve(String id) {
      return hub.entries().stream().filter(entry -> entry.command().id().equals(id)).findFirst().orElse(null);
   }

   private Component restoreTarget() {
      return previousFocus != null && previousFocus.isShowing() && previousFocus.isEnabled() && previousFocus.isFocusable()
         ? previousFocus : fallbackFocus.get();
   }

   private void afterFocus(Component target, Runnable execute) {
      if (target == null || !target.isShowing()) {
         return;
      }
      KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
      if (manager.getFocusOwner() == target) {
         SwingUtilities.invokeLater(execute);
         return;
      }
      Timer timeout = new Timer(1500, null);
      PropertyChangeListener listener = new PropertyChangeListener() {
         @Override
         public void propertyChange(java.beans.PropertyChangeEvent event) {
            if (event.getNewValue() == target) {
               manager.removePropertyChangeListener("focusOwner", this);
               timeout.stop();
               SwingUtilities.invokeLater(execute);
            }
         }
      };
      timeout.addActionListener(event -> manager.removePropertyChangeListener("focusOwner", listener));
      timeout.setRepeats(false);
      manager.addPropertyChangeListener("focusOwner", listener);
      timeout.start();
      target.requestFocus();
   }

   private void dismiss(boolean restoreFocus) {
      if (!active) {
         return;
      }
      Component target = restoreTarget();
      active = false;
      restoreCaretOnFocus = false;
      focusedIndex = -1;
      preview.hide();
      window.setVisible(false);
      visible = List.of();
      rows.clear();
      previousFocus = null;
      KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keys);
      Toolkit.getDefaultToolkit().removeAWTEventListener(outsideClicks);
      field.setText("");
      if (restoreFocus && target != null) {
         target.requestFocus();
      }
   }

   @Override
   public void close() {
      dismiss(false);
      hub.removeChangeListener(catalogChanged);
      frame.removeWindowListener(windowEvents);
      window.removeWindowListener(windowEvents);
      frame.removeComponentListener(frameEvents);
      KeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener("focusOwner", focusChanges);
      frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).remove(JaveKeyBindings.SEARCH_MENUS);
      frame.getRootPane().getActionMap().remove(FOCUS_ACTION);
      preview.close();
      window.dispose();
   }

   private static Color color(String key, Color fallback) {
      Color value = UIManager.getColor(key);
      return value == null ? fallback : value;
   }

   private final class ResultRenderer extends DefaultListCellRenderer {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean hasFocus) {
         JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, index == focusedIndex, hasFocus);
         label.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
         label.setFont(results.getFont());
         if (value instanceof MenuEntry entry) {
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            label.setEnabled(entry.isEnabled());
            label.setText(MenuSearchModel.elide(entry.command().label(), label.getFontMetrics(label.getFont()), MenuSearchModel.RESULTS_WIDTH - 22));
         } else {
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(label.getFont().deriveFont(Font.ITALIC));
            label.setEnabled(false);
            label.setText(value.toString());
            label.setBackground(list.getBackground());
         }
         return label;
      }
   }

   private static final class SearchField extends JTextField {
      @Override public Dimension getPreferredSize() { return new Dimension(180, super.getPreferredSize().height); }
      @Override public Dimension getMinimumSize() { return new Dimension(120, super.getPreferredSize().height); }
      @Override public Dimension getMaximumSize() { return getPreferredSize(); }

      @Override
      protected void paintComponent(Graphics graphics) {
         super.paintComponent(graphics);
         if (getText().isEmpty() && !hasFocus()) {
            Graphics copy = graphics.create();
            copy.setColor(color("TextField.inactiveForeground", Color.GRAY));
            copy.setFont(getFont());
            copy.drawString("Search menus", getInsets().left + 2, (getHeight() - copy.getFontMetrics().getHeight()) / 2 + copy.getFontMetrics().getAscent());
            copy.dispose();
         }
      }
   }
}
