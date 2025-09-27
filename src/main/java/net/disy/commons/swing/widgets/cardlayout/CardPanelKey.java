package net.disy.commons.swing.layout.cardlayout;

import net.disy.commons.core.util.Ensure;

public class CardPanelKey {
   private static int idCounter = 0;
   private final String id;

   private static String createNewId() {
      int id;
      synchronized (CardPanelKey.class) {
         id = idCounter++;
      }

      return String.valueOf(id);
   }

   public CardPanelKey() {
      this(createNewId());
   }

   public CardPanelKey(String id) {
      Ensure.ensureArgumentNotNull(id);
      this.id = id;
   }

   @Override
   public int hashCode() {
      return this.id.hashCode();
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof CardPanelKey)) {
         return false;
      } else {
         CardPanelKey other = (CardPanelKey)obj;
         return other.id.equals(this.id);
      }
   }

   public String getId() {
      return this.id;
   }

   @Override
   public String toString() {
      return "CardPanelKey{" + this.id + "}";
   }
}
