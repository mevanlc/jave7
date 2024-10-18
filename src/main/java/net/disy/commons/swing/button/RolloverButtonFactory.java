package net.disy.commons.swing.button;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.action.SmartToggleAction;
import net.disy.commons.swing.color.SwingColors;

public class RolloverButtonFactory {
   public static JButton createButton(SmartAction action) {
      JButton button = new JButton(action) {
         @Override
         public void setUI(ButtonUI ui) {
            super.setUI(new BasicButtonUI() {
               @Override
               public void paint(Graphics g, JComponent c) {
                  super.paint(g, c);
                  RolloverButtonFactory.paintButtonBorder(g, (AbstractButton)c);
               }
            });
         }
      };
      adjustButtonSizeAndBehavior(action, button);
      return button;
   }

   public static JToggleButton createToggleButton(Action action) {
      JToggleButton button = new JToggleButton(action) {
         @Override
         public void setUI(ButtonUI ui) {
            super.setUI(new BasicToggleButtonUI() {
               @Override
               public void paint(Graphics g, JComponent c) {
                  super.paint(g, c);
                  RolloverButtonFactory.paintButtonBorder(g, (AbstractButton)c);
               }
            });
         }
      };
      adjustButtonSizeAndBehavior(action, button);
      return button;
   }

   public static JToggleButton createToggleButton(final SmartToggleAction action) {
      final JToggleButton button = createToggleButton((Action)action);
      button.setSelected(action.getSelectionModel().getValue());
      action.getSelectionModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            button.setSelected(action.getSelectionModel().getValue());
         }
      });
      return button;
   }

   private static void adjustButtonSizeAndBehavior(Action action, AbstractButton button) {
      Icon icon = (Icon)action.getValue("SmallIcon");
      String name = (String)action.getValue("Name");
      if (icon != null && name == null) {
         button.setPreferredSize(new Dimension(icon.getIconWidth() + 7, icon.getIconHeight() + 7));
      }

      button.setFocusPainted(false);
      button.setBorderPainted(false);
      button.setRolloverEnabled(true);
   }

   private static void paintButtonBorder(Graphics g, AbstractButton button) {
      if (!button.getModel().isSelected() && (!button.getModel().isPressed() || !button.getModel().isArmed())) {
         if (button.isRolloverEnabled() && button.getModel().isRollover()) {
            Dimension size = button.getSize();
            drawSmall3dRectangleUp(g, 0, 0, size.width, size.height);
         }
      } else {
         Dimension size = button.getSize();
         drawSmall3dRectangleDown(g, 0, 0, size.width, size.height);
      }
   }

   private static void drawSmall3dRectangleDown(Graphics g, int x, int y, int w, int h) {
      g.setColor(SwingColors.getControlLtHighlightColor());
      g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
      g.drawLine(x + w - 2, y + h - 1, x, y + h - 1);
      g.setColor(SwingColors.getControlShadowColor());
      g.drawLine(x, y, x + w - 2, y);
      g.drawLine(x, y + 1, x, y + h - 2);
   }

   private static void drawSmall3dRectangleUp(Graphics g, int x, int y, int w, int h) {
      g.setColor(SwingColors.getControlLtHighlightColor());
      g.drawLine(x, y, x + w - 2, y);
      g.drawLine(x, y + 1, x, y + h - 2);
      g.setColor(SwingColors.getControlShadowColor());
      g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
      g.drawLine(x + w - 1, y + h - 1, x, y + h - 1);
   }
}
