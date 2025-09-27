package net.disy.commons.swing.dialog.input.text.component;

import javax.swing.JComponent;
import net.disy.commons.swing.dialog.input.object.component.IObjectAttributeInputComponentFactory;
import net.disy.commons.swing.dialog.input.object.component.IObjectInputComponent;

public interface IStringAttributeInputComponentFactory<C extends JComponent> extends IObjectAttributeInputComponentFactory<String, C> {
   @Override
   IObjectInputComponent<String, C> createComponent();
}
