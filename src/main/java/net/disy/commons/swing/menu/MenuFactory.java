package net.disy.commons.swing.menu;

import javax.swing.JMenu;
import net.disy.commons.swing.label.internal.MnemonicLabel;
import net.disy.commons.swing.label.internal.MnemonicLabelParser;

public class MenuFactory {
   public static JMenu createSmartMenu(String name) {
      MnemonicLabel mnemonicLabel = MnemonicLabelParser.parse(name);
      JMenu menu = new JMenu(mnemonicLabel.getPlainText());
      if (mnemonicLabel.getMnemonicCharacter() != null) {
         menu.setMnemonic(mnemonicLabel.getMnemonicCharacter());
      }

      return menu;
   }
}
