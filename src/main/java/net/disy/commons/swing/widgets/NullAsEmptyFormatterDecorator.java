package net.disy.commons.swing.text;

import java.text.ParseException;
import javax.swing.JFormattedTextField.AbstractFormatter;

public class NullAsEmptyFormatterDecorator extends AbstractFormatter {
   private final AbstractFormatter delegate;

   public NullAsEmptyFormatterDecorator(AbstractFormatter delegate) {
      this.delegate = delegate;
   }

   @Override
   public Object stringToValue(String text) throws ParseException {
      return text.trim().length() == 0 ? null : this.delegate.stringToValue(text);
   }

   @Override
   public String valueToString(Object value) throws ParseException {
      return value == null ? "" : this.delegate.valueToString(value);
   }
}
