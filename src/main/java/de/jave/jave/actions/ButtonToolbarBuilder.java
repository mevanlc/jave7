package de.jave.jave.actions;

import de.jave.gui.layout.Gap;
import de.jave.maxosx.MacOsXInitializer;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ButtonToolbarBuilder {
   private static final Dimension MINIMUM_TOOLBAR_BUTTON_SIZE = getSystemDependentMinimumToolBarButtonSize();
   private static final int ICON_PADDING = 8;
   private static final Insets TOOLBAR_BUTTON_MARGIN = new Insets(3, 3, 3, 3);
   private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));

   private static Dimension getSystemDependentMinimumToolBarButtonSize() {
      return MacOsXInitializer.isMacOs() ? new Dimension(28, 28) : new Dimension(24, 23);
   }

   public ButtonToolbarBuilder() {
      this.panel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
   }

   public void add(Action action) {
      this.panel.add(createToolbarButton(action));
   }

   public static JButton createToolbarButton(Action action) {
      JButton button = new JButton(action);
      if (action.getValue("SmallIcon") != null) {
         button.setText(null);
      }

      adjustButton(button);
      return button;
   }

   public void add(AbstractButton button) {
      adjustButton(button);
      this.panel.add(button);
   }

   public void add(JComponent component) {
      this.panel.add(component);
   }

   private static void adjustButton(AbstractButton button) {
      if (button.getIcon() != null) {
         button.setText(null);
         button.setIconTextGap(0);
      }

      Dimension size = getToolBarButtonSize(button);
      button.setPreferredSize(size);
      button.setMinimumSize(size);
      button.setMargin(TOOLBAR_BUTTON_MARGIN);
      button.setFocusPainted(false);
      button.setRolloverEnabled(true);
   }

   private static Dimension getToolBarButtonSize(AbstractButton button) {
      if (button.getIcon() == null) {
         return MINIMUM_TOOLBAR_BUTTON_SIZE;
      }

      int width = Math.max(MINIMUM_TOOLBAR_BUTTON_SIZE.width, button.getIcon().getIconWidth() + ICON_PADDING);
      int height = Math.max(MINIMUM_TOOLBAR_BUTTON_SIZE.height, button.getIcon().getIconHeight() + ICON_PADDING);
      return new Dimension(width, height);
   }

   public void addSeparator() {
      this.panel.add(new Gap(10, 1));
   }

   public JComponent createPanel() {
      return this.panel;
   }
}
