package net.disy.commons.swing.smarttable.columnsettings;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.swing.smarttable.celleditors.IntegerCellEditor;
import net.disy.commons.swing.smarttable.cellrenderers.NumberCellRenderer;

public class IntegerTableColumnSettings extends AbstractTableColumnSettings<Integer> {
   private final int stepsize;
   private final int maximum;
   private final int minimum;

   public IntegerTableColumnSettings(int minimum, int maximum, int stepsize) {
      super(2 + Math.max(String.valueOf(minimum).length(), String.valueOf(maximum).length()));
      this.minimum = minimum;
      this.maximum = maximum;
      this.stepsize = stepsize;
   }

   @Override
   public TableCellEditor getEditor() {
      return new IntegerCellEditor(this.minimum, this.maximum, this.stepsize);
   }

   @Override
   protected TableCellRenderer getBaseRenderer() {
      return new NumberCellRenderer();
   }
}
