package net.disy.commons.swing.smarttable;

import javax.swing.table.DefaultTableModel;
import net.disy.commons.core.util.ArrayUtilities;
import net.disy.commons.swing.smarttable.column.HeaderTransformer;
import net.disy.commons.swing.smarttable.column.ITableColumn;
import net.disy.commons.swing.smarttable.column.SettingsTransformer;

public class SmartTableFactory {
   private SmartTableFactory() {
   }

   public static SmartTable create(ITableColumn<?>... columns) {
      Object[] columnNames = ArrayUtilities.transform(columns, Object.class, new HeaderTransformer());
      ITableColumnViewSettings<?>[] settings = ArrayUtilities.transform(columns, getGenericColumnSettingsClass(), new SettingsTransformer());
      return new SmartTable(new DefaultTableModel(columnNames, 0), settings);
   }

   private static Class<ITableColumnViewSettings> getGenericColumnSettingsClass() {
      return ITableColumnViewSettings.class;
   }
}
