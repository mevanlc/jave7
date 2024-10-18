package net.disy.commons.swing.smarttable.column;

import net.disy.commons.swing.smarttable.ITableColumnViewSettings;

public class SmartTableColumn<T> implements ITableColumn<T> {
   private final String header;
   private final ITableColumnViewSettings<T> settings;

   public SmartTableColumn(String header, ITableColumnViewSettings<T> settings) {
      this.header = header;
      this.settings = settings;
   }

   @Override
   public String getHeader() {
      return this.header;
   }

   @Override
   public ITableColumnViewSettings<T> getSettings() {
      return this.settings;
   }
}
