package net.disy.commons.swing.dialog.input.text.component;

import javax.swing.JComponent;
import net.disy.commons.swing.dialog.input.object.IPresetValuesFactory;
import net.disy.commons.swing.dialog.input.text.IAttributeContext;

public class StringInputComponentFactoryFactory {
   public IStringAttributeInputComponentFactory<? extends JComponent> createFactory(
      IPresetValuesFactory<String> valuesFactory, IAttributeContext attributeContext
   ) {
      return valuesFactory == null
         ? new GeneralStringAttributeInputComponentFactory()
         : new PresetValuesStringAttributeInputComponentFactory(valuesFactory, attributeContext);
   }
}
