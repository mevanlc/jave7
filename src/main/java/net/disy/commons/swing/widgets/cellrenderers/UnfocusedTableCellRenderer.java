package net.disy.commons.swing.smarttable.cellrenderers;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class UnfocusedTableCellRenderer extends DefaultTableCellRenderer {
   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      return super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
   }
}
