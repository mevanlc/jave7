package net.disy.commons.swing.dialog.input.text;

import javax.swing.JPasswordField;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;
import net.disy.commons.swing.dialog.input.text.component.IStringAttributeInputComponentFactory;
import net.disy.commons.swing.dialog.input.text.component.IStringInputComponent;
import net.disy.commons.swing.dialog.input.text.component.StringTextField;

public class PasswordSmartDialogPanel extends AbstractTextSmartDialogPanel {
   public PasswordSmartDialogPanel(String label, ObjectModel<String> stringModel, IMessageProducingValidator validator) {
      super(label, stringModel, new IStringAttributeInputComponentFactory() {
         public IStringInputComponent createComponent() {
            return new StringTextField(new JPasswordField());
         }
      }, validator);
   }
}
