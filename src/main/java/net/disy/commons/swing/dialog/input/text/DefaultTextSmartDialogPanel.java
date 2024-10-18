package net.disy.commons.swing.dialog.input.text;

import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;
import net.disy.commons.swing.dialog.input.text.component.GeneralStringAttributeInputComponentFactory;
import net.disy.commons.swing.dialog.input.text.component.IStringAttributeInputComponentFactory;

public class DefaultTextSmartDialogPanel extends AbstractTextSmartDialogPanel {
   final IStringAttributeInputComponentFactory factory;

   public DefaultTextSmartDialogPanel(String label, ObjectModel<String> stringModel, IMessageProducingValidator validator) {
      this(label, stringModel, new GeneralStringAttributeInputComponentFactory(), validator);
   }

   public DefaultTextSmartDialogPanel(
      String label, ObjectModel<String> stringModel, IStringAttributeInputComponentFactory factory, IMessageProducingValidator validator
   ) {
      super(label, stringModel, factory, validator);
      this.factory = factory;
   }
}
