package de.jave.jave.actions;

import de.jave.gui.layout.Gap;
import de.jave.maxosx.MacOsXInitializer;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ButtonToolbarBuilder {
   private static final Dimension TOOLBAR_BUTTON_SIZE = getSystemDependentToolBarButtonSize();
   private final JPanel panel = new JPanel(new FlowLayout(0, 0, 2));

   private static Dimension getSystemDependentToolBarButtonSize() {
      return MacOsXInitializer.isMacOs() ? new Dimension(28, 28) : new Dimension(24, 23);
   }

   public ButtonToolbarBuilder() {
      this.panel.add(new Gap(5, 1));
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
      }

      button.setPreferredSize(TOOLBAR_BUTTON_SIZE);
      button.setFocusPainted(false);
   }

   public void addSeparator() {
      this.panel.add(new Gap(10, 1));
   }

   public JComponent createPanel() {
      return this.panel;
   }
}
