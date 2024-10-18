package net.disy.commons.swing.smarttable.cellrenderers;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.swing.color.SwingColors;
import net.disy.commons.swing.textfield.DoubleField;

public class DoubleCellRenderer implements TableCellRenderer {
   private final DoubleField field = new DoubleField();
   private static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

   public DoubleCellRenderer() {
      this.field.setBorder(null);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      this.field.setValue(((Number)value).doubleValue());
      this.field.setEnabled(table == null || table.isEnabled());
      if (isSelected) {
         this.field.setForeground(table.getSelectionForeground());
         this.field.setBackground(table.getSelectionBackground());
      } else {
         this.field.setForeground(table.getForeground());
         this.field.setBackground(table.getBackground());
      }

      if (hasFocus) {
         this.field.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
         if (table.isCellEditable(row, column)) {
            this.field.setForeground(SwingColors.getTableFocusCellForegroundColor());
            this.field.setBackground(SwingColors.getTableFocusCellBackgroundColor());
         }
      } else {
         this.field.setBorder(noFocusBorder);
      }

      return this.field.getContent();
   }
}
