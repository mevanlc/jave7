package net.disy.commons.swing.dialog.input.text.component;

import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.swing.events.AbstractDocumentChangeListener;

public class StringTextField implements IStringInputComponent<JTextField> {
   private final JTextField textField;

   public StringTextField(JTextField textField) {
      this.textField = textField;
   }

   @Override
   public void addChangeListener(final ChangeListener changeListener) {
      this.textField.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            changeListener.stateChanged(new ChangeEvent(StringTextField.this.textField));
         }
      });
   }

   public JTextField getComponent() {
      return this.textField;
   }

   public String getValue() {
      return this.textField.getText();
   }

   public void setValue(String value) {
      this.textField.setText(value);
   }

   @Override
   public boolean isEditable() {
      return this.textField.isEditable();
   }

   @Override
   public void setEditable(boolean editable) {
      this.textField.setEditable(editable);
   }

   @Override
   public void selectAll() {
      this.textField.selectAll();
   }

   @Override
   public void requestFocus() {
      this.textField.requestFocus();
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.textField.setEnabled(enabled);
   }

   @Override
   public void update() {
   }
}
