package net.disy.commons.swing.layout.cardlayout;

import java.awt.CardLayout;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.util.Ensure;

public class CardPanel {
   private final Set<CardPanelKey> knownKeys = new HashSet<>();
   private final CardLayout cardLayout = new CardLayout();
   private final JPanel panel = new JPanel(this.cardLayout);

   public void add(JComponent subPanel, CardPanelKey key) {
      Ensure.ensureArgumentNotNull(subPanel);
      Ensure.ensureArgumentNotNull(key);
      if (this.knownKeys.contains(key)) {
         throw new IllegalArgumentException("Key '" + key + "' already exists in this card panel.");
      } else {
         this.knownKeys.add(key);
         this.panel.add(subPanel, key.getId());
      }
   }

   public void setSelectedSubPanel(CardPanelKey key) {
      if (!this.knownKeys.contains(key)) {
         throw new IllegalArgumentException("No sub panel registered for the key '" + key + "'");
      } else {
         this.cardLayout.show(this.panel, key.getId());
      }
   }

   public JComponent getContent() {
      return this.panel;
   }
}
