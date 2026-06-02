package net.dizzy.commons.swing.layout.cardlayout;

import java.util.UUID;

public class CardPanelKey {
   private final String key;

   public CardPanelKey() {
      this(UUID.randomUUID().toString());
   }

   public CardPanelKey(String key) {
      this.key = key;
   }
   @Override
   public String toString() {
      return key;
   }
}
