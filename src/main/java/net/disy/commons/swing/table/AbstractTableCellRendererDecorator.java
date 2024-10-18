package net.disy.commons.swing.table;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public abstract class AbstractTableCellRendererDecorator implements TableCellRenderer {
   private TableCellRenderer renderer;

   protected TableCellRenderer getRenderer() {
      return this.renderer;
   }

   public void setDelegate(TableCellRenderer renderer) {
      this.renderer = renderer;
   }

   protected JComponent getOriginalRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      return (JComponent)this.getRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
   }
}
