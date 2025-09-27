package net.disy.commons.swing.smarttable;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.core.util.StringUtilities;

public class IconTableHeaderRenderer extends JLabel implements TableCellRenderer {
   private final Icon icon;
   private final String toolTipText;

   public IconTableHeaderRenderer(Icon icon, String toolTipText) {
      this.icon = icon;
      this.toolTipText = toolTipText;
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      JTableHeader header = table.getTableHeader();
      if (header != null) {
         this.setForeground(header.getForeground());
         this.setBackground(header.getBackground());
         this.setFont(header.getFont());
      }

      this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
      this.setHorizontalAlignment(0);
      this.setIcon(this.icon);
      if (this.toolTipText != null) {
         this.setToolTipText(this.toolTipText);
      } else {
         String valueText = value == null ? null : String.valueOf(value);
         if (!StringUtilities.isNullOrEmpty(valueText)) {
            this.setToolTipText(valueText);
         }
      }

      return this;
   }
}
