package net.disy.commons.swing.dialog.tabbed.internal;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import net.disy.commons.swing.color.SwingColors;
import net.disy.commons.swing.dialog.tabbed.TabPlacement;
import net.disy.commons.swing.util.GuiUtilities;

public class SmartTabbedPaneUi extends BasicTabbedPaneUI {
   private static final String PROPERTY_ACTIVE_WINDOW = "activeWindow";
   private static final String PROPERTY_FOCUS_OWNER = "focusOwner";
   private static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
   private final HierarchyListener hierarchyListener = new HierarchyListener() {
      @Override
      public void hierarchyChanged(HierarchyEvent e) {
         SmartTabbedPaneUi.this.updateFocusListenerAttached();
      }
   };
   private final Color darkActiveColor = createWeightedAverageColor(SystemColor.activeCaption, 1, SwingColors.getControlColor(), 2);
   private final Color lightActiveColor = createWeightedAverageColor(SystemColor.activeCaption, 1, SwingColors.getControlLtHighlightColor(), 5);
   private final Color darkInactiveColor = createWeightedAverageColor(SystemColor.inactiveCaption, 2, SwingColors.getControlColor(), 1);
   private final Color lightInactiveColor = createWeightedAverageColor(SystemColor.inactiveCaption, 1, SwingColors.getControlColor(), 3);
   private PropertyChangeListener focusManagerListener;

   private static Color createWeightedAverageColor(Color color1, int weight1, Color color2, int weight2) {
      int red = createWeightedAverage(color1.getRed(), weight1, color2.getRed(), weight2);
      int green = createWeightedAverage(color1.getGreen(), weight1, color2.getGreen(), weight2);
      int blue = createWeightedAverage(color1.getBlue(), weight1, color2.getBlue(), weight2);
      int alpha = createWeightedAverage(color1.getAlpha(), weight1, color2.getAlpha(), weight2);
      return new Color(red, green, blue, alpha);
   }

   private static int createWeightedAverage(int value1, int weight1, int value2, int weight2) {
      return (value1 * weight1 + value2 * weight2) / (weight1 + weight2);
   }

   @Override
   public void installUI(JComponent c) {
      super.installUI(c);
      c.addHierarchyListener(this.hierarchyListener);
   }

   @Override
   public void uninstallUI(JComponent c) {
      c.removeHierarchyListener(this.hierarchyListener);
      super.uninstallUI(c);
      this.assureFocusListenerDetached();
   }

   private void updateFocusListenerAttached() {
      if (this.tabPane != null && this.tabPane.isDisplayable()) {
         this.assureFocusListenerAttached();
      } else {
         this.assureFocusListenerDetached();
      }
   }

   private void assureFocusListenerDetached() {
      if (this.focusManagerListener != null) {
         KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
         focusManager.removePropertyChangeListener(this.focusManagerListener);
         this.focusManagerListener = null;
      }
   }

   private void assureFocusListenerAttached() {
      if (this.focusManagerListener == null) {
         this.focusManagerListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
               if ("focusOwner".equals(evt.getPropertyName())) {
                  SmartTabbedPaneUi.this.tabPane.repaint();
               } else if ("activeWindow".equals(evt.getPropertyName())) {
                  SmartTabbedPaneUi.this.tabPane.repaint();
               }
            }
         };
         KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
         focusManager.addPropertyChangeListener(this.focusManagerListener);
      }
   }

   @Override
   protected void paintFocusIndicator(
      Graphics g, int tabPlacement, Rectangle[] rectangles, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected
   ) {
   }

   @Override
   protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
      return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) + 5;
   }

   @Override
   protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
      if (isSelected) {
         Graphics2D graphics = (Graphics2D)g;
         TabActiveState state = getTabActiveState((ExtendedJTabbedPane)this.tabPane, tabIndex);
         switch (state) {
            case ACTIVE:
               this.paintTabBackground(graphics, x, y, w, h, this.darkActiveColor, this.lightActiveColor);
               break;
            case DISABLED:
               this.paintTabBackground(graphics, x, y, w, h, this.lightActiveColor, TRANSPARENT_COLOR);
               break;
            case FOCUS_OUTSIDE:
               Color controlColor = SwingColors.getControlColor();
               this.paintTabBackground(graphics, x, y, w, h, controlColor, controlColor);
               break;
            case INACTIVE:
               this.paintTabBackground(graphics, x, y, w, h, this.darkInactiveColor, this.lightInactiveColor);
         }
      }
   }

   private static TabActiveState getTabActiveState(ExtendedJTabbedPane tabPane, int tabIndex) {
      if (!GuiUtilities.isContainedInActiveWindow(tabPane)) {
         return TabActiveState.INACTIVE;
      } else if (!GuiUtilities.containsFocusOwner(tabPane) && !tabPane.isPopupMenuVisible()) {
         return TabActiveState.FOCUS_OUTSIDE;
      } else {
         return tabIndex != -1 && tabPane.isEnabledAt(tabIndex) ? TabActiveState.ACTIVE : TabActiveState.DISABLED;
      }
   }

   private void paintTabBackground(Graphics2D graphics, int x, int y, int w, int h, Color startColor, Color endColor) {
      TabPlacement placement = ((ExtendedJTabbedPane)this.tabPane).getTabTitlePlacement();
      Point startPoint;
      Point endPoint;
      switch (placement) {
         case BOTTOM:
            endPoint = new Point(x, y + h - 2);
            startPoint = new Point(x, y + 2);
            break;
         case LEFT:
            startPoint = new Point(x + w, y);
            endPoint = new Point(x, y);
            break;
         case RIGHT:
            startPoint = new Point(x, y);
            endPoint = new Point(x + w, y);
            break;
         case TOP:
            startPoint = new Point(x, y + h - 2);
            endPoint = new Point(x, y + 2);
            break;
         default:
            throw new RuntimeException("Unsupported tab placement " + placement);
      }

      GradientPaint gradient = new GradientPaint(startPoint, startColor, endPoint, endColor);
      graphics.setPaint(gradient);
      graphics.fillRect(x, y, w, h);
   }

   @Override
   protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
      TabActiveState state = getTabActiveState((ExtendedJTabbedPane)this.tabPane, this.tabPane.getSelectedIndex());
      Color lightColor = this.lightHighlight;
      Color color = TRANSPARENT_COLOR;
      switch (state) {
         case ACTIVE:
            color = this.darkActiveColor;
            lightColor = this.darkActiveColor.brighter();
         case DISABLED:
         case FOCUS_OUTSIDE:
         default:
            break;
         case INACTIVE:
            color = this.darkInactiveColor;
            lightColor = this.darkInactiveColor.brighter();
      }

      Rectangle selRect = selectedIndex < 0 ? null : this.getTabBounds(selectedIndex, this.calcRect);
      g.setColor(lightColor);
      if (tabPlacement == 1 && selectedIndex >= 0 && selRect.y + selRect.height + 1 >= y && selRect.x >= x && selRect.x <= x + w) {
         g.drawLine(x, y, selRect.x - 1, y);
         if (selRect.x + selRect.width < x + w - 2) {
            g.drawLine(selRect.x + selRect.width, y, x + w - 2, y);
         } else {
            g.setColor(color);
            g.drawLine(x + w - 2, y, x + w - 2, y);
         }

         g.setColor(color);
         g.drawLine(selRect.x, y, selRect.x + selRect.width, y);
      } else {
         g.drawLine(x, y, x + w - 2, y);
      }

      if (state == TabActiveState.ACTIVE || state == TabActiveState.INACTIVE) {
         g.setColor(color);
         g.drawLine(x, y + 1, x + w - 2, y + 1);
         g.drawLine(x, y + 2, x + w - 2, y + 2);
      }
   }

   @Override
   protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
      TabActiveState state = getTabActiveState((ExtendedJTabbedPane)this.tabPane, this.tabPane.getSelectedIndex());
      Color lightColor = this.lightHighlight;
      Color color = TRANSPARENT_COLOR;
      switch (state) {
         case ACTIVE:
            lightColor = this.darkActiveColor.brighter();
            color = this.darkActiveColor;
         case DISABLED:
         case FOCUS_OUTSIDE:
         default:
            break;
         case INACTIVE:
            lightColor = this.darkInactiveColor.brighter();
            color = this.darkInactiveColor;
      }

      Rectangle selRect = selectedIndex < 0 ? null : this.getTabBounds(selectedIndex, this.calcRect);
      g.setColor(lightColor);
      if (tabPlacement == 2 && selectedIndex >= 0 && selRect.x + selRect.width + 1 >= x && selRect.y >= y && selRect.y <= y + h) {
         g.drawLine(x, y, x, selRect.y - 1);
         if (selRect.y + selRect.height < y + h - 2) {
            g.drawLine(x, selRect.y + selRect.height, x, y + h - 2);
         }

         if (state == TabActiveState.ACTIVE || state == TabActiveState.INACTIVE) {
            g.setColor(color);
            g.drawLine(x, selRect.y + 1, x, selRect.y + selRect.height - 1);
            g.drawLine(x + 1, selRect.y, x + 1, selRect.y + selRect.height);
         }
      } else {
         g.drawLine(x, y, x, y + h - 2);
      }

      if (state == TabActiveState.ACTIVE || state == TabActiveState.INACTIVE) {
         g.setColor(color);
         g.drawLine(x + 1, y + 1, x + 1, y + h - 2);
      }
   }

   @Override
   protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
      TabActiveState state = getTabActiveState((ExtendedJTabbedPane)this.tabPane, this.tabPane.getSelectedIndex());
      Color color = this.shadow;
      Color darkerColor = this.darkShadow;
      switch (state) {
         case ACTIVE:
            darkerColor = this.darkActiveColor.darker();
            color = this.darkActiveColor;
         case DISABLED:
         case FOCUS_OUTSIDE:
         default:
            break;
         case INACTIVE:
            darkerColor = this.darkInactiveColor.darker();
            color = this.darkInactiveColor;
      }

      Rectangle selRect = selectedIndex < 0 ? null : this.getTabBounds(selectedIndex, this.calcRect);
      g.setColor(color);
      if (tabPlacement == 3 && selectedIndex >= 0 && selRect.y - 1 <= h && selRect.x >= x && selRect.x <= x + w) {
         g.drawLine(x + 1, y + h - 2, selRect.x - 1, y + h - 2);
         g.setColor(darkerColor);
         g.drawLine(x, y + h - 1, selRect.x - 1, y + h - 1);
         if (selRect.x + selRect.width < x + w - 2) {
            g.setColor(color);
            g.drawLine(selRect.x + selRect.width, y + h - 2, x + w - 2, y + h - 2);
            g.setColor(darkerColor);
            g.drawLine(selRect.x + selRect.width, y + h - 1, x + w - 1, y + h - 1);
         }

         if (state == TabActiveState.ACTIVE || state == TabActiveState.INACTIVE) {
            g.setColor(color);
            g.drawLine(selRect.x, y + h - 1, selRect.x + selRect.width, y + h - 1);
            g.drawLine(selRect.x, y + h - 2, selRect.x + selRect.width, y + h - 2);
         }
      } else {
         if (state == TabActiveState.ACTIVE || state == TabActiveState.INACTIVE) {
            g.drawLine(x + 1, y + h - 3, x + w - 2, y + h - 3);
         }

         g.drawLine(x + 1, y + h - 2, x + w - 2, y + h - 2);
         g.setColor(darkerColor);
         g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
      }
   }

   @Override
   protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
      TabActiveState state = getTabActiveState((ExtendedJTabbedPane)this.tabPane, this.tabPane.getSelectedIndex());
      Color color = this.shadow;
      Color darkerColor = this.darkShadow;
      switch (state) {
         case ACTIVE:
            darkerColor = this.darkActiveColor.darker();
            color = this.darkActiveColor;
         case DISABLED:
         case FOCUS_OUTSIDE:
         default:
            break;
         case INACTIVE:
            darkerColor = this.darkInactiveColor.darker();
            color = this.darkInactiveColor;
      }

      Rectangle selRect = selectedIndex < 0 ? null : this.getTabBounds(selectedIndex, this.calcRect);
      g.setColor(color);
      if (tabPlacement == 4 && selectedIndex >= 0 && selRect.x - 1 <= w && selRect.y >= y && selRect.y <= y + h) {
         g.drawLine(x + w - 2, y + 1, x + w - 2, selRect.y - 1);
         g.setColor(darkerColor);
         g.drawLine(x + w - 1, y, x + w - 1, selRect.y - 1);
         if (selRect.y + selRect.height < y + h - 2) {
            g.setColor(color);
            g.drawLine(x + w - 2, selRect.y + selRect.height, x + w - 2, y + h - 2);
            g.setColor(darkerColor);
            g.drawLine(x + w - 1, selRect.y + selRect.height, x + w - 1, y + h - 2);
         }

         if (state == TabActiveState.ACTIVE || state == TabActiveState.INACTIVE) {
            g.setColor(color);
            g.drawLine(x + w - 1, selRect.y, x + w - 1, selRect.y + selRect.height - 1);
            g.drawLine(x + w - 2, selRect.y, x + w - 2, selRect.y + selRect.height);
         }
      } else {
         if (state == TabActiveState.ACTIVE || state == TabActiveState.INACTIVE) {
            g.drawLine(x + w - 3, y + 1, x + w - 3, y + h - 3);
         }

         g.drawLine(x + w - 2, y + 1, x + w - 2, y + h - 3);
         g.setColor(darkerColor);
         g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
      }
   }
}
