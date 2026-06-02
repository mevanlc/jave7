package net.dizzy.commons.swing.label.internal;

public class MnemonicLabel {
   private final String plainText;
   private final Character mnemonicCharacter;

   public MnemonicLabel(String plainText, Character mnemonicCharacter) {
      this.plainText = plainText;
      this.mnemonicCharacter = mnemonicCharacter;
   }

   public String getPlainText() {
      return plainText;
   }

   public Character getMnemonicCharacter() {
      return mnemonicCharacter;
   }
}
