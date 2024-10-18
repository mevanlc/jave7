package net.disy.commons.swing.smarttable.columnsettings;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.smarttable.celleditors.NullValueStrategy;
import net.disy.commons.swing.smarttable.celleditors.NumberCellEditor;
import net.disy.commons.swing.smarttable.cellrenderers.NumberCellRenderer;

public class NumberTableColumnSettings extends AbstractTableColumnSettings<Number> {
   private final NumberFormat format;
   private final NullValueStrategy nullValueStrategy;
   private final Class<? extends Number> valueClass;

   public static NumberTableColumnSettings getDoubleInstance(String format, NullValueStrategy nullValueStrategy) {
      return new NumberTableColumnSettings(new DecimalFormat(format), Double.class, nullValueStrategy);
   }

   public static NumberTableColumnSettings getWholeNumberInstance(Class<? extends Number> clazz) {
      return getWholeNumberInstance(clazz, null);
   }

   public static NumberTableColumnSettings getWholeNumberInstance(Class<? extends Number> clazz, Color background) {
      return new NumberTableColumnSettings(new DecimalFormat("####"), clazz, NullValueStrategy.EMPTY, background);
   }

   public static NumberTableColumnSettings getDoubleNumberInstance(Class<? extends Number> clazz, Color background) {
      return new NumberTableColumnSettings(new DecimalFormat(), clazz, NullValueStrategy.EMPTY, background);
   }

   public static NumberTableColumnSettings getIntegerInstance(String format, NullValueStrategy nullValueStrategy) {
      return new NumberTableColumnSettings(new DecimalFormat(format), Integer.class, nullValueStrategy);
   }

   public NumberTableColumnSettings(NumberFormat format, Class<? extends Number> valueClass) {
      this(format, valueClass, NullValueStrategy.DISALLOW);
   }

   public NumberTableColumnSettings(NumberFormat format, Class<? extends Number> valueClass, NullValueStrategy nullValueStrategy) {
      this(format, valueClass, nullValueStrategy, null);
   }

   public NumberTableColumnSettings(NumberFormat format, Class<? extends Number> valueClass, NullValueStrategy nullValueStrategy, Color background) {
      super(format.getMinimumIntegerDigits() + format.getMinimumFractionDigits() + 2, background);
      Ensure.ensureArgumentNotNull(format);
      Ensure.ensureArgumentNotNull(valueClass);
      Ensure.ensureArgumentNotNull(nullValueStrategy);
      this.format = format;
      this.valueClass = valueClass;
      this.nullValueStrategy = nullValueStrategy;
   }

   @Override
   public TableCellEditor getEditor() {
      return new NumberCellEditor(this.format, this.valueClass, this.nullValueStrategy);
   }

   @Override
   protected TableCellRenderer getBaseRenderer() {
      return new NumberCellRenderer(this.format);
   }
}
