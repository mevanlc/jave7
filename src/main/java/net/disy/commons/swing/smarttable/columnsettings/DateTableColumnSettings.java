package net.disy.commons.swing.smarttable.columnsettings;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.core.util.DateRange;
import net.disy.commons.swing.smarttable.celleditors.DateCellEditor;
import net.disy.commons.swing.smarttable.celleditors.NullValueStrategy;
import net.disy.commons.swing.smarttable.cellrenderers.StringCellRenderer;

public class DateTableColumnSettings extends AbstractTableColumnSettings<Date> {
   private static final String DEFAULT_FORMAT = "dd.MM.yyyy";
   private final NullValueStrategy nullValueStrategy;
   private final DateCellEditor editor;
   private final DateFormat format;

   public DateTableColumnSettings() {
      this(NullValueStrategy.DISALLOW);
   }

   public DateTableColumnSettings(NullValueStrategy nullValueStrategy) {
      this("dd.MM.yyyy", nullValueStrategy);
   }

   public DateTableColumnSettings(String formatString, NullValueStrategy nullValueStrategy) {
      super(formatString.length());
      this.nullValueStrategy = nullValueStrategy;
      this.format = new SimpleDateFormat(formatString);
      this.editor = new DateCellEditor(this.format, nullValueStrategy);
   }

   @Override
   public TableCellEditor getEditor() {
      return this.editor;
   }

   @Override
   protected TableCellRenderer getBaseRenderer() {
      return new StringCellRenderer() {
         @Override
         public Component getTableCellRendererComponent(JTable table, Object valueObject, boolean isSelected, boolean hasFocus, int row, int column) {
            Object value = valueObject == null
               ? DateTableColumnSettings.this.nullValueStrategy.getStringValue()
               : DateTableColumnSettings.this.format.format(valueObject);
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         }
      };
   }

   public void setAllowedRange(DateRange range) {
      this.editor.setAllowedRange(range);
   }
}
