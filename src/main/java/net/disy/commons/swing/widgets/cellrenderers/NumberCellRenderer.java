package net.disy.commons.swing.smarttable.cellrenderers;

import java.awt.Component;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JTable;

public class NumberCellRenderer extends StringCellRenderer {
   private final NumberFormat format;

   public NumberCellRenderer() {
      this(NumberFormat.getNumberInstance());
   }

   public NumberCellRenderer(NumberFormat format) {
      this.format = format;
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      JLabel component = (JLabel)super.getTableCellRendererComponent(table, value == null ? "" : this.format.format(value), isSelected, hasFocus, row, column);
      component.setHorizontalAlignment(4);
      return component;
   }
}
