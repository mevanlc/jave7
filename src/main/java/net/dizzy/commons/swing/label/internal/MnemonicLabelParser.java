package net.dizzy.commons.swing.label.internal;

public final class MnemonicLabelParser {
   private MnemonicLabelParser() {
   }

   public static MnemonicLabel parse(String text) {
      StringBuilder builder = new StringBuilder();
      Character mnemonic = null;
      for (int i = 0; i < text.length(); i++) {
         char ch = text.charAt(i);
         if (ch == '&' && i + 1 < text.length()) {
            char next = text.charAt(++i);
            if (next == '&') {
               builder.append('&');
            } else {
               if (mnemonic == null) {
                  mnemonic = Character.valueOf(next);
               }
               builder.append(next);
            }
         } else {
            builder.append(ch);
         }
      }
      return new MnemonicLabel(builder.toString(), mnemonic);
   }
}
