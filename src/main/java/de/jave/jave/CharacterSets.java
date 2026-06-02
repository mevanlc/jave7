package de.jave.jave;

import net.dizzy.commons.core.util.Ensure;

public class CharacterSets {
   private static final int NONE = 0;
   private static final int PURE_ASCII = 1;
   private static final int UNICODE = 2;
   private static final int DEFAULT_CHARSET_INDEX = UNICODE;
   private static int currentCharsetIndex = DEFAULT_CHARSET_INDEX;
   private final CharSetsConfiguration configuration;

   public static final int getDefaultCharsetIndex() {
      return DEFAULT_CHARSET_INDEX;
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
         if (currentCharsetIndex == PURE_ASCII) {
            return ch >= ' ' && ch <= '~';
         } else if (currentCharsetIndex == UNICODE) {
            return isLegalUnicodeBmpCharacter(ch);
         }

         return this.configuration.getCharSetChatacters(currentCharsetIndex).indexOf(ch) != -1;
      }
   }

   private static boolean isLegalUnicodeBmpCharacter(char ch) {
      return !isAsciiControlCharacter(ch) && !Character.isSurrogate(ch) && Character.UnicodeBlock.of(ch) != Character.UnicodeBlock.PRIVATE_USE_AREA;
   }

   private static boolean isAsciiControlCharacter(char ch) {
      return ch < ' ' || ch == '\u007f';
   }

   public String[] getCharsetNames() {
      return this.configuration.getCharsetNames();
   }
}
