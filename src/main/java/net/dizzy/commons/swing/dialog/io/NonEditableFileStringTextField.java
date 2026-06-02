package net.dizzy.commons.swing.dialog.io;

import javax.swing.JComponent;
import javax.swing.JTextField;

import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.swing.component.IComponentContainer;

public class NonEditableFileStringTextField implements IComponentContainer {
   private final JTextField textField = new JTextField();

   public NonEditableFileStringTextField(ObjectModel<String> fileNameModel) {
      textField.setEditable(false);
      textField.setText(fileNameModel.getValue());
      fileNameModel.addChangeListener(() -> textField.setText(fileNameModel.getValue()));
   }

   @Override public JComponent getContent() { return textField; }
}
