package net.dizzy.commons.swing.label;

import java.awt.Component;

import javax.swing.JLabel;

import net.dizzy.commons.swing.label.internal.MnemonicLabel;
import net.dizzy.commons.swing.label.internal.MnemonicLabelParser;

public class SmartLabel extends JLabel {
   public SmartLabel(String text) {
      super();
      apply(text);
   }

   public SmartLabel(String text, Component labelFor) {
      this(text);
      setLabelFor(labelFor);
   }

   private void apply(String text) {
      MnemonicLabel label = MnemonicLabelParser.parse(text);
      setText(label.getPlainText());
      if (label.getMnemonicCharacter() != null) {
         setDisplayedMnemonic(label.getMnemonicCharacter().charValue());
      }
   }
}
