package net.disy.commons.swing.label.internal;

public class MnemonicLabelParser {
   public static MnemonicLabel parse(String label) {
      int index = -1;
      boolean found = false;

      do {
         index = label.indexOf(38, ++index);
         if (index != -1 && index + 1 < label.length()) {
            if (label.charAt(index + 1) != '&') {
               found = true;
               break;
            }

            label = label.substring(0, index) + label.substring(index + 1);
         }
      } while (index != -1 && index + 1 < label.length());

      if (found) {
         char mnemonic = label.charAt(index + 1);
         return new MnemonicLabel(label.substring(0, index) + label.substring(index + 1), mnemonic);
      } else {
         return new MnemonicLabel(label, null);
      }
   }
}
