package net.dizzy.commons.swing.textfield;

import javax.swing.JComponent;
import javax.swing.JTextField;

import net.dizzy.commons.swing.component.IComponentContainer;
import net.dizzy.commons.swing.events.AbstractDocumentChangeListener;

public class DoubleField implements IComponentContainer {
   private final JTextField textField;

   public DoubleField(int columns) {
      textField = new JTextField(columns);
   }

   public void setValue(double value) {
      textField.setText(Double.toString(value));
   }

   public double getValue() {
      try {
         return Double.parseDouble(textField.getText());
      } catch (NumberFormatException exception) {
         return 0;
      }
   }

   public void addDocumentListener(AbstractDocumentChangeListener listener) {
      textField.getDocument().addDocumentListener(listener);
   }

   @Override public JComponent getContent() { return textField; }
}
