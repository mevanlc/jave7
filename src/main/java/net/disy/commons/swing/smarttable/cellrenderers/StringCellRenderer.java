package net.disy.commons.swing.smarttable.cellrenderers;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class StringCellRenderer extends DefaultTableCellRenderer {
   private final boolean showTooltip;

   public StringCellRenderer() {
      this(false);
   }

   public StringCellRenderer(boolean showTooltip) {
      this.showTooltip = showTooltip;
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      JComponent component = (JComponent)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      component.setEnabled(table.isEnabled());
      if (this.showTooltip) {
         component.setToolTipText(value.toString());
      }

      return component;
   }
}
