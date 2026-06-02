package de.jave.jave.actions;

import de.jave.maxosx.MacOsXInitializer;
import javax.swing.JMenu;
import net.dizzy.commons.swing.label.internal.MnemonicLabel;
import net.dizzy.commons.swing.label.internal.MnemonicLabelParser;

public class SmartMenu extends JMenu {
   public SmartMenu(String text) {
      MnemonicLabel mnemonicLabel = MnemonicLabelParser.parse(text);
      this.setText(mnemonicLabel.getPlainText());
      Character mnemonicCharacter = mnemonicLabel.getMnemonicCharacter();
      if (mnemonicCharacter != null) {
         MacOsXInitializer.setMnemonic(this, mnemonicCharacter);
      }
   }
}
