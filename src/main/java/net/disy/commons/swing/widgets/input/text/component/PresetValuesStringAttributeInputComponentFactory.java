package net.disy.commons.swing.dialog.input.text.component;

import javax.swing.JComboBox;
import net.disy.commons.swing.dialog.input.object.IPresetValuesFactory;
import net.disy.commons.swing.dialog.input.text.IAttributeContext;

public class PresetValuesStringAttributeInputComponentFactory implements IStringAttributeInputComponentFactory<JComboBox> {
   private final IPresetValuesFactory<String> presetValuesFactory;
   private final IAttributeContext attributeContext;

   public PresetValuesStringAttributeInputComponentFactory(IPresetValuesFactory<String> presetValueListFactory, IAttributeContext attributeContext) {
      this.presetValuesFactory = presetValueListFactory;
      this.attributeContext = attributeContext;
   }

   public IStringInputComponent<JComboBox> createComponent() {
      return new StringComboBox(this.presetValuesFactory, this.attributeContext);
   }
}
