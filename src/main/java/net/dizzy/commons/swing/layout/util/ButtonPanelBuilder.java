package net.dizzy.commons.swing.layout.util;

import java.awt.GridLayout;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanelBuilder {
   private final JPanel panel = new JPanel(new GridLayout(1, 0, LayoutUtilities.getComponentSpacing(), 0));

   public ButtonPanelBuilder() {
      this(LayoutDirection.HORIZONTAL);
   }

   public ButtonPanelBuilder(LayoutDirection direction) {
      if (direction == LayoutDirection.VERTICAL) {
         panel.setLayout(new GridLayout(0, 1, 0, LayoutUtilities.getComponentSpacing()));
      }
   }

   public ButtonPanelBuilder add(Action action) {
      panel.add(new JButton(action));
      return this;
   }

   public ButtonPanelBuilder add(JButton button) {
      panel.add(button);
      return this;
   }

   public ButtonPanelBuilder add(JButton... buttons) {
      for (JButton button : buttons) {
         add(button);
      }
      return this;
   }

   public JPanel createPanel() {
      return panel;
   }
}
