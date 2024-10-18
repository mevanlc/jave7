package net.disy.commons.swing.dialog.input.object.component;

import javax.swing.JComponent;

public interface IObjectAttributeInputComponentFactory<O, C extends JComponent> {
   IObjectInputComponent<O, C> createComponent();
}
