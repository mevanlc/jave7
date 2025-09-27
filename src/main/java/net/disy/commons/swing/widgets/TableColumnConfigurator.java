package net.disy.commons.swing.smarttable;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TableColumnConfigurator {
   public static void configureTableColumns(JTable table, ITableColumnViewSettings<?>[] settings) {
      TableColumnModel columnModel = table.getColumnModel();

      for (int columnIndex = 0; columnIndex < settings.length; columnIndex++) {
         TableColumn tableColumn = columnModel.getColumn(columnIndex);
         ITableColumnViewSettings<?> view = settings[columnIndex];
         tableColumn.setCellEditor(view.getEditor());
         if (view.getRenderer() != null) {
            tableColumn.setCellRenderer(view.getRenderer());
         }

         tableColumn.setPreferredWidth(view.getPreferredWidth());
         tableColumn.setWidth(view.getPreferredWidth());
         if (!view.isResizable()) {
            tableColumn.setResizable(view.isResizable());
            tableColumn.setMinWidth(view.getPreferredWidth());
            tableColumn.setMaxWidth(view.getPreferredWidth());
         }

         Icon icon = view.getIcon();
         if (icon != null) {
            tableColumn.setHeaderRenderer(new IconTableHeaderRenderer(icon, view.getToolTipText()));
         }
      }
   }
}
