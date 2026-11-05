package de.jave.jave.preferences;

public enum GlyphEmulation {
   JAVE("Jave"),
   KITTY("Kitty");

   private final String displayName;

   GlyphEmulation(String displayName) {
      this.displayName = displayName;
   }

   public String getDisplayName() {
      return this.displayName;
   }
}
