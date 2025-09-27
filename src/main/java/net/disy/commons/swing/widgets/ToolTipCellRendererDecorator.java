package net.disy.commons.swing.table;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;

public class ToolTipCellRendererDecorator extends AbstractTableCellRendererDecorator {
   private final ITableHeaderToolTipProvider tooltipProvider;

   public ToolTipCellRendererDecorator(ITableHeaderToolTipProvider provider) {
      this.tooltipProvider = provider;
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      JComponent component = this.getOriginalRendererComponent(table, value, isSelected, hasFocus, row, column);
      component.setToolTipText(this.tooltipProvider.getToolTip(value, column));
      return component;
   }
}
