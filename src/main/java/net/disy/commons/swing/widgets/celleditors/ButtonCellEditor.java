package net.disy.commons.swing.smarttable.celleditors;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.smarttable.columnsettings.IButtonEditorConfiguration;
import net.disy.commons.swing.smarttable.columnsettings.IEditStoppedHandler;

public class ButtonCellEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
   private final IButtonEditorConfiguration configuration;
   private Object editorValue;

   public ButtonCellEditor(IButtonEditorConfiguration configuration) {
      Ensure.ensureArgumentNotNull(configuration);
      this.configuration = configuration;
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int columnIndex) {
      return this.createButtonComponent(table, rowIndex, columnIndex, value);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
      return this.createButtonComponent(table, rowIndex, columnIndex, value);
   }

   @Override
   public boolean shouldSelectCell(EventObject anEvent) {
      return true;
   }

   @Override
   public boolean isCellEditable(EventObject anEvent) {
      return anEvent instanceof MouseEvent ? ((MouseEvent)anEvent).getClickCount() >= 1 : true;
   }

   @Override
   public Object getCellEditorValue() {
      return this.editorValue;
   }

   private Component createButtonComponent(JTable table, int rowIndex, int columnIndex, Object value) {
      this.editorValue = value;
      SmartAction action = this.configuration.createAction(new IEditStoppedHandler() {
         @Override
         public void editCanceled() {
            ButtonCellEditor.this.fireEditingCanceled();
         }

         @Override
         public void editFinished(Object newValue) {
            ButtonCellEditor.this.editorValue = newValue;
            ButtonCellEditor.this.fireEditingStopped();
         }
      }, rowIndex, columnIndex, value);
      JButton button = createButton(action);
      button.setEnabled(table.isEnabled() && table.isCellEditable(rowIndex, columnIndex));
      return button;
   }

   private static JButton createButton(SmartAction action) {
      JButton button = new JButton(action);
      if (action.getIcon() != null && action.getName() == null) {
         button.setMargin(new Insets(0, 0, 0, 0));
      }

      return button;
   }

   public static int getButtonWidth(String longestButtonLabel, Icon largestButtonIcon) {
      return createButton(new SmartAction(longestButtonLabel, largestButtonIcon) {
         @Override
         protected void execute(Component parentComponent) {
         }
      }).getPreferredSize().width;
   }
}
