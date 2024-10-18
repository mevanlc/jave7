package de.jave.jave;

import net.disy.commons.core.util.Ensure;

public class CharacterSets {
   private static final int NONE = 0;
   private static final int PURE_ASCII = 1;
   private static final int DEFAULT_CHARSET_INDEX = 1;
   private static int currentCharsetIndex = 1;
   private final CharSetsConfiguration configuration;

   public static final int getDefaultCharsetIndex() {
      return 1;
   }

   public CharacterSets(CharSetsConfiguration configuration) {
      Ensure.ensureArgumentNotNull(configuration);
      this.configuration = configuration;
   }

   public void setCurrentCharsetIndex(int index) {
      currentCharsetIndex = index;
   }

   public boolean isLegal(char ch) {
      if (currentCharsetIndex == 0) {
         return true;
      } else {
         return currentCharsetIndex != 1 ? this.configuration.getCharSetChatacters(currentCharsetIndex).indexOf(ch) != -1 : ch >= ' ' && ch <= '~';
      }
   }

   public String[] getCharsetNames() {
      return this.configuration.getCharsetNames();
   }
}
