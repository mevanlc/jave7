package net.disy.commons.core.text.font;

public enum FontStyleProperty {
   BOLD("Bold"),
   ITALICS("Italics");

   private final String name;

   private FontStyleProperty(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }
}
