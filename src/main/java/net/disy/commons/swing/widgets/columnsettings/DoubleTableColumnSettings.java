package net.disy.commons.swing.smarttable.columnsettings;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.swing.smarttable.celleditors.DoubleCellEditor;
import net.disy.commons.swing.smarttable.cellrenderers.DoubleCellRenderer;

public class DoubleTableColumnSettings extends AbstractTableColumnSettings<Double> {
   public DoubleTableColumnSettings() {
      this(6);
   }

   public DoubleTableColumnSettings(int preferredColumnCount) {
      super(preferredColumnCount);
   }

   @Override
   public TableCellEditor getEditor() {
      return new DoubleCellEditor();
   }

   @Override
   protected TableCellRenderer getBaseRenderer() {
      return new DoubleCellRenderer();
   }
}
