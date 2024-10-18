package net.disy.commons.swing.smarttable.columnsettings;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellEditor;
import net.disy.commons.core.util.CastingTransformer;
import net.disy.commons.core.util.ITransformer;

public class CheckBoxTableColumnSettings extends AbstractCheckBoxTableColumnSettings<Boolean> {
   private final ITransformer<? super Object, Boolean> valueTransformer;

   public CheckBoxTableColumnSettings() {
      this(2);
   }

   public CheckBoxTableColumnSettings(int columnCount) {
      this(columnCount, null);
   }

   public CheckBoxTableColumnSettings(int columnCount, Icon icon) {
      this(columnCount, icon, new CastingTransformer<>());
   }

   public CheckBoxTableColumnSettings(int columnCount, Icon icon, ITransformer<Object, Boolean> valueTransformer) {
      super(columnCount, icon);
      this.valueTransformer = valueTransformer;
   }

   @Override
   public TableCellEditor getEditor() {
      return new DefaultCellEditor(new JCheckBox()) {
         @Override
         public Component getComponent() {
            return WrapInPanelAsCenteredComponentDecorator.wrapInPanelAsCentered(super.getComponent());
         }

         @Override
         public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return WrapInPanelAsCenteredComponentDecorator.wrapInPanelAsCentered(
               super.getTableCellEditorComponent(table, CheckBoxTableColumnSettings.this.transform(value), isSelected, row, column)
            );
         }

         @Override
         public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
            return WrapInPanelAsCenteredComponentDecorator.wrapInPanelAsCentered(
               super.getTreeCellEditorComponent(tree, CheckBoxTableColumnSettings.this.transform(value), isSelected, expanded, leaf, row)
            );
         }
      };
   }

   @Override
   protected JCheckBox createRenderCheckBox(Object value) {
      JCheckBox checkBox = new JCheckBox();
      checkBox.setSelected(this.transform(value));
      return checkBox;
   }

   private Boolean transform(Object value) {
      return this.valueTransformer.transform(value);
   }
}
