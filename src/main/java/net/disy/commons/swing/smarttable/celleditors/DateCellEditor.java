package net.disy.commons.swing.smarttable.celleditors;

import java.text.DateFormat;
import java.text.Format;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.DateFormatter;
import net.disy.commons.core.util.DateRange;

public class DateCellEditor extends AbstractFormattedCellEditor {
   private DateFormatter formatter;

   public DateCellEditor(DateFormat format, NullValueStrategy nullValueStrategy) {
      super(format, nullValueStrategy, 2);
   }

   @Override
   protected AbstractFormatter createFormatter(Format format) {
      this.formatter = new DateFormatter((DateFormat)format);
      return this.formatter;
   }

   public void setAllowedRange(DateRange range) {
      this.formatter.setMinimum(range.getFirstDay());
      this.formatter.setMaximum(range.getLastDay());
   }
}
