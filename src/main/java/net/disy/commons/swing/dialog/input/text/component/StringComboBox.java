package net.disy.commons.swing.dialog.input.text.component;

import javax.swing.JComboBox;
import net.disy.commons.swing.dialog.input.object.IPresetValuesFactory;
import net.disy.commons.swing.dialog.input.object.component.AbstractObjectComboBox;
import net.disy.commons.swing.dialog.input.text.IAttributeContext;
import net.disy.commons.swing.ui.ToStringUi;

public class StringComboBox extends AbstractObjectComboBox<String> implements IStringInputComponent<JComboBox> {
   public StringComboBox(IPresetValuesFactory<String> presetValuesFactory, IAttributeContext attributeContext) {
      super(presetValuesFactory, attributeContext, new ToStringUi<>());
   }
}
