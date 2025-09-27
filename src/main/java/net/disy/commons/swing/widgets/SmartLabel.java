package net.disy.commons.swing.label;

import javax.swing.JComponent;
import javax.swing.JLabel;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.label.internal.MnemonicLabel;
import net.disy.commons.swing.label.internal.MnemonicLabelParser;

public class SmartLabel extends JLabel {
   public SmartLabel(String label, IComponentContainer componentContainer) {
      this(label, componentContainer.getContent());
   }

   public SmartLabel(String label) {
      this(label, (JComponent)null);
   }

   public SmartLabel(String label, JComponent forComponent) {
      this.setText(label);
      if (forComponent != null) {
         this.setLabelFor(forComponent);
      }
   }

   @Override
   public void setText(String text) {
      MnemonicLabel mnemonicLabel = MnemonicLabelParser.parse(text);
      if (mnemonicLabel.getMnemonicCharacter() != null) {
         this.setDisplayedMnemonic(mnemonicLabel.getMnemonicCharacter());
      }

      super.setText(mnemonicLabel.getPlainText());
   }
}
