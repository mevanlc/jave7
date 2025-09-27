package net.disy.commons.swing.smarttable.columnsettings;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class BackgroundColorTableCellRendererDecorator implements TableCellRenderer {
   private final TableCellRenderer renderer;
   private final Color background;

   public BackgroundColorTableCellRendererDecorator(TableCellRenderer renderer, Color background) {
      this.renderer = renderer;
      this.background = background;
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      Component component = this.renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      if (component.getBackground().equals(table.getBackground())) {
         component.setBackground(this.background);
      }

      return component;
   }
}
