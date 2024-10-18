package net.disy.commons.swing.smarttable;

public interface ISmartTableConfiguration {
   int getVisibleRowCount();

   ITableColumnViewSettings<?>[] getColumnViewSettings();
}
