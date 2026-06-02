package de.jave.jave.pixelplate;

import net.dizzy.commons.core.util.Ensure;

public class PixelPlateConfiguration {
   public static PixelPlateConfiguration INSTANCE;
   private final char[] rules;
   private final char[] rulesThick;

   public PixelPlateConfiguration(char[] rules, char[] rulesThick) {
      Ensure.ensureArgumentNotNull(rules);
      Ensure.ensureArgumentNotNull(rulesThick);
      this.rules = rules;
      this.rulesThick = rulesThick;
      INSTANCE = this;
   }

   public char[] getRulesThick() {
      return this.rulesThick;
   }

   public char[] getRules() {
      return this.rules;
   }
}
