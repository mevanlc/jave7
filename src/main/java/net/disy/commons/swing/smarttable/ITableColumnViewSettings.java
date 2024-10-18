package net.disy.commons.swing.smarttable;

import javax.swing.Icon;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public interface ITableColumnViewSettings<T> {
   TableCellEditor getEditor();

   IObjectSelectionStrategy<T> getDoubleClickBehaviour();

   TableCellRenderer getRenderer();

   boolean isResizable();

   int getPreferredWidth();

   Icon getIcon();

   String getToolTipText();
}
