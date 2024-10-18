package net.disy.commons.swing.smarttable.columnsettings;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.swing.color.SwingColors;

public abstract class AbstractCheckBoxTableColumnSettings<T> extends AbstractTableColumnSettings<T> {
   public AbstractCheckBoxTableColumnSettings() {
      this(2);
   }

   public AbstractCheckBoxTableColumnSettings(int columnCount) {
      this(columnCount, null);
   }

   public AbstractCheckBoxTableColumnSettings(int columnCount, Icon icon) {
      super(columnCount, icon);
   }

   protected abstract JCheckBox createRenderCheckBox(Object var1);

   @Override
   protected TableCellRenderer getBaseRenderer() {
      return new TableCellRenderer() {
         @Override
         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
            JCheckBox checkBox = AbstractCheckBoxTableColumnSettings.this.createRenderCheckBox(value);
            checkBox.setEnabled(table.isEnabled() && table.isCellEditable(rowIndex, columnIndex));
            JPanel panel = WrapInPanelAsCenteredComponentDecorator.wrapInPanelAsCentered(checkBox);
            if (hasFocus && table.isCellEditable(rowIndex, columnIndex)) {
               checkBox.setForeground(SwingColors.getTableFocusCellForegroundColor());
               panel.setForeground(SwingColors.getTableFocusCellForegroundColor());
               checkBox.setBackground(SwingColors.getTableFocusCellBackgroundColor());
               panel.setBackground(SwingColors.getTableFocusCellBackgroundColor());
            } else if (isSelected) {
               checkBox.setForeground(table.getSelectionForeground());
               panel.setForeground(table.getSelectionForeground());
               checkBox.setBackground(table.getSelectionBackground());
               panel.setBackground(table.getSelectionBackground());
            } else {
               checkBox.setForeground(table.getForeground());
               panel.setForeground(table.getForeground());
               checkBox.setBackground(table.getBackground());
               panel.setBackground(table.getBackground());
            }

            return panel;
         }
      };
   }

   @Override
   public boolean isResizable() {
      return false;
   }
}
