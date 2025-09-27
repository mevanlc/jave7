package net.disy.commons.swing.dialog.input.text.component;

import javax.swing.JTextField;

public class GeneralStringAttributeInputComponentFactory implements IStringAttributeInputComponentFactory<JTextField> {
   public IStringInputComponent<JTextField> createComponent() {
      return new StringTextField(new JTextField());
   }
}
