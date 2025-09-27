package net.disy.commons.swing.dialog.input.text;

import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;
import net.disy.commons.swing.dialog.input.object.AbstractObjectSmartDialogPanel;
import net.disy.commons.swing.dialog.input.text.component.IStringAttributeInputComponentFactory;

public abstract class AbstractTextSmartDialogPanel extends AbstractObjectSmartDialogPanel<String> {
   public AbstractTextSmartDialogPanel(
      String label, ObjectModel<String> stringModel, IStringAttributeInputComponentFactory<?> componentFactory, IMessageProducingValidator validator
   ) {
      super(label, stringModel, componentFactory, validator);
   }
}
