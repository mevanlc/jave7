package net.disy.commons.swing.smarttable.celleditors;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.color.widgets.ColorModel;
import net.disy.commons.swing.dialog.color.ColorChooserLabel;
import net.disy.commons.swing.dialog.color.DefaultColorChooserConfiguration;

public class ColorCellEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
   private ColorModel colorModel;
   private final boolean enabledIfNotEditable;

   public ColorCellEditor(boolean enabledIfNotEditable) {
      this.enabledIfNotEditable = enabledIfNotEditable;
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int columnIndex) {
      return this.createColorComponent(table, rowIndex, columnIndex, (Color)value);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
      return this.createColorComponent(table, rowIndex, columnIndex, (Color)value);
   }

   @Override
   public boolean shouldSelectCell(EventObject anEvent) {
      return true;
   }

   @Override
   public boolean isCellEditable(EventObject anEvent) {
      return anEvent instanceof MouseEvent ? ((MouseEvent)anEvent).getClickCount() >= 2 : true;
   }

   @Override
   public Object getCellEditorValue() {
      return this.colorModel.getColor();
   }

   private Component createColorComponent(JTable table, int rowIndex, int columnIndex, Color color) {
      this.colorModel = new ColorModel(color);
      ColorChooserLabel colorChooseLabel = new ColorChooserLabel(this.colorModel, new DefaultColorChooserConfiguration(true));
      colorChooseLabel.setEnabled(table.isEnabled() && (this.enabledIfNotEditable || table.isCellEditable(rowIndex, columnIndex)));
      this.colorModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            ColorCellEditor.this.fireEditingStopped();
         }
      });
      colorChooseLabel.getContent().addFocusListener(new FocusListener() {
         @Override
         public void focusGained(FocusEvent e) {
         }

         @Override
         public void focusLost(FocusEvent e) {
            ColorCellEditor.this.fireEditingStopped();
         }
      });
      colorChooseLabel.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            ColorCellEditor.this.fireEditingStopped();
         }
      });
      return colorChooseLabel.getContent();
   }
}
