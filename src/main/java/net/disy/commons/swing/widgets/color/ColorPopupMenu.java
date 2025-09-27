package net.disy.commons.swing.dialog.color;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JPopupMenu.Separator;
import javax.swing.plaf.MenuItemUI;
import javax.swing.plaf.basic.BasicMenuItemUI;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.color.SwingColors;
import net.disy.commons.swing.layout.grid.EndOfLineMarkerComponent;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.resources.DisyCommonsSwingMessages;

public abstract class ColorPopupMenu extends JPopupMenu {
   private static final int COLUMN_COUNT = 6;
   private static final Color[] DEFAULT_COLORS = new Color[]{
      Color.WHITE,
      Color.LIGHT_GRAY,
      Color.GRAY,
      Color.DARK_GRAY,
      Color.BLACK,
      Color.PINK,
      Color.RED,
      Color.YELLOW,
      Color.GREEN,
      Color.CYAN,
      Color.BLUE,
      Color.MAGENTA,
      new Color(128, 0, 0),
      new Color(128, 128, 0),
      new Color(0, 128, 0),
      new Color(0, 128, 128),
      new Color(0, 0, 128),
      new Color(128, 0, 128),
      Color.ORANGE
   };

   public ColorPopupMenu(boolean transparencyEnabled) {
      this.setLayout(new GridDialogLayout(6, true, LayoutUtilities.getDpiAdjusted(2), LayoutUtilities.getDpiAdjusted(2)));
      GridDialogLayoutData data = new GridDialogLayoutData();
      data.setHorizontalSpan(6);
      if (transparencyEnabled) {
         this.add(new JMenuItem(new SmartAction(DisyCommonsSwingMessages.getString("ColorPopupMenu.Action.Transparent.label")) {
            @Override
            protected void execute(Component parentComponent) {
               ColorPopupMenu.this.setColor(new Color(0, 0, 0, 0));
            }
         }), data);
         this.add(new Separator(), data);
      }

      for (int i = 0; i < DEFAULT_COLORS.length; i++) {
         this.addColorItem(DEFAULT_COLORS[i]);
      }

      this.add(new EndOfLineMarkerComponent());
      this.add(new Separator(), data);
      this.add(new JMenuItem(new SmartAction(DisyCommonsSwingMessages.getString("ColorPopupMenu.Action.MoreColors.label")) {
         @Override
         protected void execute(Component parentComponent) {
            ColorPopupMenu.this.performColorChooserDialog(parentComponent);
         }
      }), data);
   }

   protected abstract void performColorChooserDialog(Component var1);

   protected abstract void setColor(Color var1);

   private void addColorItem(final Color color) {
      Icon icon = new Icon() {
         @Override
         public int getIconHeight() {
            return 16;
         }

         @Override
         public int getIconWidth() {
            return 16;
         }

         @Override
         public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y, this.getIconWidth(), this.getIconHeight());
            g.setColor(SwingColors.getControlDkShadowColor());
            g.drawLine(x, y, this.getIconWidth(), y);
            g.drawLine(x, y, x, y + this.getIconHeight() - 1);
            g.setColor(SwingColors.getControlLtHighlightColor());
            g.drawLine(x, y + this.getIconHeight() - 1, this.getIconWidth(), y + this.getIconHeight() - 1);
            g.drawLine(x + this.getIconWidth() - 1, y, x + this.getIconWidth() - 1, y + this.getIconHeight() - 1);
         }
      };
      JMenuItem menuItem = new JMenuItem(new SmartAction(icon) {
         @Override
         protected void execute(Component parentComponent) {
            ColorPopupMenu.this.setColor(color);
         }
      }) {
         @Override
         public void setUI(MenuItemUI ui) {
            super.setUI(
               new BasicMenuItemUI() {
                  @Override
                  public Dimension getPreferredSize(JComponent component) {
                     JMenuItem menuItem = (JMenuItem)component;
                     return new Dimension(menuItem.getIcon().getIconWidth() + 4, menuItem.getIcon().getIconHeight() + 2);
                  }

                  @Override
                  protected void paintMenuItem(
                     Graphics g, JComponent component, Icon checkIcon, Icon arrowIcon, Color background, Color foreground, int defaultTextIconGap
                  ) {
                     JMenuItem menuItem = (JMenuItem)component;
                     this.paintBackground(g, menuItem, background);
                     if (menuItem.getIcon() != null) {
                        Icon paintIcon;
                        if (!model.isEnabled()) {
                           paintIcon = menuItem.getDisabledIcon();
                        } else if (model.isPressed() && model.isArmed()) {
                           paintIcon = menuItem.getPressedIcon();
                           if (paintIcon == null) {
                              paintIcon = menuItem.getIcon();
                           }
                        } else {
                           paintIcon = menuItem.getIcon();
                        }

                        if (paintIcon != null) {
                           paintIcon.paintIcon(component, g, 2, 1);
                        }
                     }
                  }
               }
            );
         }
      };
      this.add(menuItem);
   }
}
