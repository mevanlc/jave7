package net.disy.commons.swing.smarttable.celleditors;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.color.widgets.ColorModel;
import net.disy.commons.swing.dialog.color.ColorChooserLabel;
import net.disy.commons.swing.dialog.color.DefaultColorChooserConfiguration;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.util.ToggleComponentEnabler;

public class ColorModificationCellEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
   private ColorModel colorModel;
   private JCheckBox checkBox;

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      return this.createCheckboxColorComponent((IColorModificationEntry)value);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      return this.createCheckboxColorComponent((IColorModificationEntry)value);
   }

   @Override
   public Object getCellEditorValue() {
      return new IColorModificationEntry() {
         @Override
         public boolean isModified() {
            return ColorModificationCellEditor.this.checkBox.isSelected();
         }

         @Override
         public Color getColor() {
            return ColorModificationCellEditor.this.colorModel.getColor();
         }
      };
   }

   private Component createCheckboxColorComponent(IColorModificationEntry cell) {
      this.checkBox = new JCheckBox("", cell.isModified());
      this.colorModel = new ColorModel(cell.getColor());
      ColorChooserLabel colorChooseButton = new ColorChooserLabel(this.colorModel, new DefaultColorChooserConfiguration(true));
      ToggleComponentEnabler.connect(this.checkBox, colorChooseButton.getContent());
      this.checkBox.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            ColorModificationCellEditor.this.fireEditingStopped();
         }
      });
      this.colorModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            ColorModificationCellEditor.this.fireEditingStopped();
         }
      });
      colorChooseButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            ColorModificationCellEditor.this.fireEditingStopped();
         }
      });
      JPanel panel = new JPanel(new GridDialogLayout(3, false));
      panel.add(Box.createHorizontalStrut(2));
      panel.add(this.checkBox);
      panel.add(colorChooseButton.getContent(), GridDialogLayoutData.FILL_HORIZONTAL);
      panel.addFocusListener(new FocusListener() {
         @Override
         public void focusGained(FocusEvent e) {
         }

         @Override
         public void focusLost(FocusEvent e) {
            ColorModificationCellEditor.this.fireEditingStopped();
         }
      });
      return panel;
   }
}
