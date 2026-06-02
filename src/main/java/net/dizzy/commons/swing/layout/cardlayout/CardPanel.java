package net.dizzy.commons.swing.layout.cardlayout;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class CardPanel {
   private final CardLayout layout = new CardLayout();
   private final JPanel panel = new JPanel(layout);

   public void add(Component component, CardPanelKey key) {
      panel.add(component, key.toString());
   }

   public void setSelectedSubPanel(CardPanelKey key) {
      layout.show(panel, key.toString());
   }

   public JComponent getContent() {
      return panel;
   }
}
