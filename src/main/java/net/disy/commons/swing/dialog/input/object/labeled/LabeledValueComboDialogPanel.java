package net.disy.commons.swing.dialog.input.object.labeled;

import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;
import net.disy.commons.swing.dialog.input.object.AbstractObjectSmartDialogPanel;
import net.disy.commons.swing.dialog.input.object.IPresetValuesFactory;
import net.disy.commons.swing.dialog.input.object.component.IObjectAttributeInputComponentFactory;
import net.disy.commons.swing.dialog.input.object.component.IObjectInputComponent;
import net.disy.commons.swing.dialog.input.text.IAttributeContext;

public class LabeledValueComboDialogPanel<T> extends AbstractObjectSmartDialogPanel<T> {
   public LabeledValueComboDialogPanel(
      String label,
      final ObjectModel<T> model,
      IMessageProducingValidator validator,
      final IPresetValuesFactory<ILabeledValue> presetValuesFactory,
      final IAttributeContext attributeContext
   ) {
      super(label, model, new IObjectAttributeInputComponentFactory<T, JComboBox>() {
         @Override
         public IObjectInputComponent<T, JComboBox> createComponent() {
            final LabeledValueComboBox<T> labeledValueComboBox = new LabeledValueComboBox<>(presetValuesFactory, attributeContext);
            labeledValueComboBox.setValue(model.getValue());
            labeledValueComboBox.addChangeListener(new ChangeListener() {
               @Override
               public void stateChanged(ChangeEvent event) {
                  model.setValue(labeledValueComboBox.getValue());
               }
            });
            return labeledValueComboBox;
         }
      }, validator);
   }
}
