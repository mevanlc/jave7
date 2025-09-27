package net.disy.commons.swing.smarttable.cellrenderers;

import java.awt.Component;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.swing.laf.LookAndFeelUtilities;

public class ComboBoxTableCellRenderer extends JComboBox implements TableCellRenderer {
   public ComboBoxTableCellRenderer(Object[] items) {
      super(items);
   }

   public ComboBoxTableCellRenderer() {
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      boolean cellEditable = table.isCellEditable(row, column);
      LookAndFeelUtilities.adjustCell(this, table, isSelected, hasFocus, cellEditable);
      if (value instanceof String && !this.containsItem(value)) {
         this.addItem(value);
      }

      this.setSelectedItem(value);
      this.setEnabled(table.isEnabled() && cellEditable);
      return this;
   }

   private boolean containsItem(Object value) {
      ComboBoxModel model = this.getModel();

      for (int i = 0; i < model.getSize(); i++) {
         if (value.equals(model.getElementAt(i))) {
            return true;
         }
      }

      return false;
   }
}
