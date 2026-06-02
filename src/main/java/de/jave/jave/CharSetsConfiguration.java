package de.jave.jave;

import net.dizzy.commons.core.util.Ensure;

public class CharSetsConfiguration {
   private final String[] charsetNames;
   private final String[] charsetCharacters;
   private final int userDefinedIndex;

   public CharSetsConfiguration(String[] charsetNames, String[] charsetCharacters) {
      Ensure.ensureArgumentNotNull(charsetNames);
      Ensure.ensureArgumentNotNull(charsetCharacters);
      this.charsetNames = charsetNames;
      this.charsetCharacters = charsetCharacters;
      this.userDefinedIndex = charsetNames.length - 1;
   }

   public int getUserDefinedIndex() {
      return this.userDefinedIndex;
   }

   public String getCharSetChatacters(int index) {
      return this.charsetCharacters[index];
   }

   public void setCharsetCharacters(int index, String string) {
      this.charsetCharacters[index] = string;
   }

   public String[] getCharsetNames() {
      return this.charsetNames;
   }
}
