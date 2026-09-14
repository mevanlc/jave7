package de.jave.jave.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.MenuSelectionManager;
import javax.swing.UIManager;

/** Shows the original menus as a location preview without enabling them or taking keyboard focus. */
final class MenuLocationPreview implements AutoCloseable {
   private record PopupState(JMenu menu, boolean focusable, boolean selected) {}
   private final List<PopupState> shown = new ArrayList<>();
   private final JWindow marker;
   private JMenuItem target;

   MenuLocationPreview(JFrame owner) {
      marker = new JWindow(owner);
      marker.setType(Window.Type.POPUP);
      marker.setFocusableWindowState(false);
      marker.setAutoRequestFocus(false);
      marker.setBackground(new Color(0, 0, 0, 0));
      marker.setContentPane(new JPanel() {
         @Override
         protected void paintComponent(Graphics graphics) {
            Color accent = UIManager.getColor("TextField.selectionBackground");
            graphics.setColor(accent == null ? new Color(40, 110, 220) : accent);
            graphics.fillPolygon(new int[]{0, 13, 0}, new int[]{0, 7, 14}, 3);
         }
      });
      ((JPanel)marker.getContentPane()).setOpaque(false);
      marker.setSize(14, 15);
   }

   void show(MenuEntry entry, Rectangle resultsBounds) {
      hide();
      MenuSelectionManager.defaultManager().clearSelectedPath();
      for (JMenu menu : entry.parents()) {
         JPopupMenu popup = menu.getPopupMenu();
         shown.add(new PopupState(menu, popup.isFocusable(), menu.isSelected()));
         popup.setFocusable(false);
         menu.setSelected(true);
         Point origin = menu.getLocationOnScreen();
         Dimension size = popup.getPreferredSize();
         int x = menu.isTopLevelMenu() ? origin.x : origin.x + menu.getWidth();
         int y = menu.isTopLevelMenu() ? origin.y + menu.getHeight() : origin.y;
         Rectangle bounds = position(new Rectangle(x, y, size.width, size.height), resultsBounds, screenBounds(menu));
         // JMenu.setPopupMenuVisible refuses disabled ancestors. Showing their actual popup
         // directly preserves their disabled state and leaves execution guarded by the hub.
         popup.show(menu, bounds.x - origin.x, bounds.y - origin.y);
      }
      target = entry.item();
      target.setArmed(true);
      Point point = target.getLocationOnScreen();
      Rectangle screen = screenBounds(target);
      marker.setLocation(Math.max(screen.x, point.x - marker.getWidth()), point.y + (target.getHeight() - marker.getHeight()) / 2);
      marker.setVisible(true);
   }

   static Rectangle position(Rectangle desired, Rectangle reserved, Rectangle screen) {
      Rectangle result = new Rectangle(desired);
      result.x = Math.max(screen.x, Math.min(result.x, screen.x + screen.width - result.width));
      result.y = Math.max(screen.y, Math.min(result.y, screen.y + screen.height - result.height));
      if (result.intersects(reserved)) {
         result.x = Math.max(screen.x, reserved.x - result.width - 6);
      }
      return result;
   }

   static Rectangle screenBounds(java.awt.Component component) {
      var configuration = component.getGraphicsConfiguration();
      Rectangle screen = new Rectangle(configuration.getBounds());
      Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(configuration);
      screen.x += insets.left;
      screen.y += insets.top;
      screen.width -= insets.left + insets.right;
      screen.height -= insets.top + insets.bottom;
      return screen;
   }

   void hide() {
      marker.setVisible(false);
      MenuSelectionManager.defaultManager().clearSelectedPath();
      if (target != null) {
         target.setArmed(false);
         target = null;
      }
      for (int i = shown.size() - 1; i >= 0; i--) {
         PopupState state = shown.get(i);
         state.menu().getPopupMenu().setVisible(false);
         state.menu().setSelected(state.selected());
         state.menu().getPopupMenu().setFocusable(state.focusable());
      }
      shown.clear();
   }

   @Override
   public void close() {
      hide();
      marker.dispose();
   }
}
