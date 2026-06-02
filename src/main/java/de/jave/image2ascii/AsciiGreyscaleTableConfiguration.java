package de.jave.image2ascii;

import java.util.Map;
import net.dizzy.commons.core.util.Ensure;

public class AsciiGreyscaleTableConfiguration {
   private final String[] tableNames;
   private final Map<String, AsciiGreyScaleTableItem> tableItemsByName;
   private final String defaultTableName;

   public AsciiGreyscaleTableConfiguration(String[] tableNames, String defaultTableName, Map<String, AsciiGreyScaleTableItem> tableItemsByName) {
      Ensure.ensureArgumentNotNull(tableNames);
      Ensure.ensureArgumentNotNull(defaultTableName);
      Ensure.ensureArgumentNotNull(tableItemsByName);
      this.tableNames = tableNames;
      this.defaultTableName = defaultTableName;
      this.tableItemsByName = tableItemsByName;
   }

   public AsciiGreyScaleTableItem getAsciiGreyscaleTableItem(String name) {
      return this.tableItemsByName.get(name);
   }

   public AsciiGreyScaleTableItem getDefaultTableItem() {
      return this.getAsciiGreyscaleTableItem(this.defaultTableName);
   }

   public AsciiGreyScaleTableItem[] getTableItems() {
      AsciiGreyScaleTableItem[] tables = new AsciiGreyScaleTableItem[this.tableNames.length];

      for (int i = 0; i < tables.length; i++) {
         tables[i] = this.getAsciiGreyscaleTableItem(this.tableNames[i]);
      }

      return tables;
   }

   public AsciiGreyscaleTable getDefaultTable() {
      AsciiGreyScaleTableItem defaultTableItem = this.getDefaultTableItem();
      return defaultTableItem.getGreyscaleTable();
   }

   public AsciiGreyscaleTable getTable(String name) {
      AsciiGreyScaleTableItem item = this.getAsciiGreyscaleTableItem(name);
      return item == null ? null : item.getGreyscaleTable();
   }
}
