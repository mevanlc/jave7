package net.disy.commons.swing.smarttable.columnsettings;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.core.number.MaxIntegerValueBuilder;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.laf.LookAndFeelUtilities;
import net.disy.commons.swing.smarttable.IObjectSelectionStrategy;
import net.disy.commons.swing.smarttable.ITableColumnViewSettings;
import net.disy.commons.swing.smarttable.cellrenderers.ComboBoxTableCellRenderer;
import net.disy.commons.swing.smarttable.columnsettings.preferredwidth.ComboBoxPreferredWidth;
import net.disy.commons.swing.smarttable.columnsettings.preferredwidth.IPreferredWidth;
import net.disy.commons.swing.smarttable.columnsettings.preferredwidth.TextPreferredWidth;
import net.disy.commons.swing.ui.IObjectUi;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class ComboBoxTableColumnSettings<T> implements ITableColumnViewSettings<T> {
   private final ComboBoxTableColumnSettings<T>.ComboBoxRenderer editComboBox = new ComboBoxTableColumnSettings.ComboBoxRenderer();
   private final ComboBoxTableColumnSettings<T>.ComboBoxRenderer renderComboBox = new ComboBoxTableColumnSettings.ComboBoxRenderer();
   private int preferredWidth;
   private IComboBoxValuesProvider<T> valuesProvider;

   public ComboBoxTableColumnSettings(T[] values) {
      this(values, defaultPreferredColumnCount(values, null));
   }

   private static int defaultPreferredColumnCount(Object[] values, IObjectUi<Object> objectUi) {
      MaxIntegerValueBuilder builder = new MaxIntegerValueBuilder(0);

      for (int i = 0; i < values.length; i++) {
         Object value = values[i];
         String stringRepresentation = objectUi == null ? String.valueOf(value) : objectUi.getLabel(value);
         builder.add(stringRepresentation.length());
      }

      return builder.getMaximum() + 1;
   }

   public ComboBoxTableColumnSettings(IComboBoxValuesProvider<T> valuesProvider) {
      this(valuesProvider, null);
   }

   public ComboBoxTableColumnSettings(IComboBoxValuesProvider<T> valuesProvider, IObjectUi<T> objectUi) {
      this.valuesProvider = valuesProvider;
      if (objectUi != null) {
         this.editComboBox.setRenderer(new ObjectUiListCellRenderer(objectUi));
         this.renderComboBox.setRenderer(new ObjectUiListCellRenderer(objectUi));
      }
   }

   public ComboBoxTableColumnSettings(T[] values, int preferredColumnCount) {
      this(values, preferredColumnCount, null);
   }

   public ComboBoxTableColumnSettings(T[] values, int preferredColumnCount, IObjectUi<T> objectUi) {
      this(new ConstantValuesProvider<>(values), preferredColumnCount, objectUi);
   }

   public ComboBoxTableColumnSettings(T[] values, IObjectUi<T> objectUi) {
      this(new ConstantValuesProvider<>(values), new ComboBoxPreferredWidth<>(values, objectUi), objectUi);
   }

   public ComboBoxTableColumnSettings(IComboBoxValuesProvider<T> valuesProvider, int preferredColumnCount, IObjectUi<T> objectUi) {
      this(valuesProvider, new TextPreferredWidth(preferredColumnCount), objectUi);
   }

   private ComboBoxTableColumnSettings(IComboBoxValuesProvider<T> valuesProvider, IPreferredWidth preferredWidth, IObjectUi<T> objectUi) {
      this.valuesProvider = valuesProvider;
      this.preferredWidth = preferredWidth.getPreferredWidth();
      if (objectUi != null) {
         this.editComboBox.setRenderer(new ObjectUiListCellRenderer(objectUi));
         this.renderComboBox.setRenderer(new ObjectUiListCellRenderer(objectUi));
      }
   }

   @Override
   public TableCellEditor getEditor() {
      return new DefaultCellEditor(this.editComboBox) {
         @Override
         public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            ComboBoxTableColumnSettings.this.editComboBox.setDirectValues(ComboBoxTableColumnSettings.this.valuesProvider.getValues(row));
            LookAndFeelUtilities.adjustCell(ComboBoxTableColumnSettings.this.editComboBox, table, isSelected, true, true);
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
         }
      };
   }

   @Override
   public TableCellRenderer getRenderer() {
      return this.renderComboBox;
   }

   public void setValues(T[] values) {
      this.valuesProvider = new ConstantValuesProvider<>(values);
   }

   @Override
   public boolean isResizable() {
      return true;
   }

   @Override
   public int getPreferredWidth() {
      return this.preferredWidth > 0 ? this.preferredWidth : this.editComboBox.getPreferredSize().width;
   }

   public void setTextEditable(boolean textEditable) {
      this.editComboBox.setEditable(textEditable);
   }

   @Override
   public IObjectSelectionStrategy<T> getDoubleClickBehaviour() {
      return new NullDoubleClickBehaviour<>();
   }

   @Override
   public Icon getIcon() {
      return null;
   }

   @Override
   public String getToolTipText() {
      return null;
   }

   private class ComboBoxRenderer extends ComboBoxTableCellRenderer {
      private ComboBoxRenderer() {
      }

      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         this.setDirectValues(ComboBoxTableColumnSettings.this.valuesProvider.getValues(row));
         return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      }

      public void setDirectValues(T[] values) {
         Ensure.ensureArgumentNotNull(values);
         this.removeAllItems();

         for (int i = 0; i < values.length; i++) {
            this.addItem(values[i]);
         }
      }
   }
}
