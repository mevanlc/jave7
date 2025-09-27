package net.disy.commons.swing.smarttable;

import net.disy.commons.core.util.Ensure;

public class SmartTableConfiguration implements ISmartTableConfiguration {
   private final ITableColumnViewSettings<? extends Object>[] columnViewSettings;
   private final int visibleRowCount;

   public SmartTableConfiguration(ITableColumnViewSettings<?>... columnViewSettings) {
      this(6, columnViewSettings);
   }

   @Deprecated
   public SmartTableConfiguration(ITableColumnViewSettings<? extends Object>[] columnViewSettings, int visibleRowCount) {
      this(visibleRowCount, columnViewSettings);
   }

   public SmartTableConfiguration(int visibleRowCount, ITableColumnViewSettings<?>... columnViewSettings) {
      Ensure.ensureArgumentNotNull(columnViewSettings);
      this.columnViewSettings = columnViewSettings;
      this.visibleRowCount = visibleRowCount;
   }

   @Override
   public ITableColumnViewSettings<? extends Object>[] getColumnViewSettings() {
      return this.columnViewSettings;
   }

   @Override
   public int getVisibleRowCount() {
      return this.visibleRowCount;
   }
}
