package net.disy.commons.swing.smarttable.column;

import net.disy.commons.swing.smarttable.ITableColumnViewSettings;

public interface ITableColumn<T> {
   String getHeader();

   ITableColumnViewSettings<T> getSettings();
}
