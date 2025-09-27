package net.disy.commons.swing.smarttable.celleditors;

import java.text.Format;
import java.text.NumberFormat;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.NumberFormatter;

public class NumberCellEditor extends AbstractFormattedCellEditor {
   private NumberFormatter formatter;

   public NumberCellEditor(NumberFormat format, Class<?> valueClass, NullValueStrategy nullValueStrategy) {
      super(format, nullValueStrategy, 4);
      this.formatter.setValueClass(valueClass);
   }

   @Override
   protected AbstractFormatter createFormatter(Format format) {
      this.formatter = new NumberFormatter((NumberFormat)format);
      this.formatter.setAllowsInvalid(false);
      this.formatter.setCommitsOnValidEdit(true);
      return this.formatter;
   }
}
