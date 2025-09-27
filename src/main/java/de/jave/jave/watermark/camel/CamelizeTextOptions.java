package de.jave.jave.algorithm.camel;

import net.disy.commons.core.util.Ensure;

public class CamelizeTextOptions {
   private char fillCharacter = ':';
   private CamelizeTextPreserveMode preserveMode = CamelizeTextPreserveMode.PRESERVE_WHITESPACE;

   public char getFillCharacter() {
      return this.fillCharacter;
   }

   public CamelizeTextPreserveMode getPreserveMode() {
      return this.preserveMode;
   }

   public void setPreserveMode(CamelizeTextPreserveMode preserveMode) {
      Ensure.ensureArgumentNotNull(preserveMode);
      this.preserveMode = preserveMode;
   }

   public void setFillCharacter(char fillCharacter) {
      this.fillCharacter = fillCharacter;
   }
}
