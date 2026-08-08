package de.jave.lib;

import net.dizzy.commons.core.util.Ensure;

public class CharacterMergeRulesConfiguration {
   public static CharacterMergeRulesConfiguration INSTANCE;
   private final char[] mixCharacters1;
   private final char[] mixCharacters2;
   private final char[] mixResults;

   public CharacterMergeRulesConfiguration(char[] mixCharacters1, char[] mixCharacters2, char[] mixResults) {
      Ensure.ensureArgumentNotNull(mixCharacters1);
      Ensure.ensureArgumentNotNull(mixCharacters2);
      Ensure.ensureArgumentNotNull(mixResults);
      this.mixCharacters1 = mixCharacters1;
      this.mixCharacters2 = mixCharacters2;
      this.mixResults = mixResults;
      INSTANCE = this;
   }

   public int getMergeResult(int previousCharacter, int newCharacter) {
      if (previousCharacter < Character.MIN_VALUE
         || previousCharacter > Character.MAX_VALUE
         || newCharacter < Character.MIN_VALUE
         || newCharacter > Character.MAX_VALUE) {
         return newCharacter;
      }

      for (int i = 0; i < this.mixCharacters1.length; i++) {
         if (this.mixCharacters1[i] == previousCharacter && this.mixCharacters2[i] == newCharacter
            || this.mixCharacters2[i] == previousCharacter && this.mixCharacters1[i] == newCharacter) {
            return this.mixResults[i];
         }
      }

      return newCharacter;
   }
}
