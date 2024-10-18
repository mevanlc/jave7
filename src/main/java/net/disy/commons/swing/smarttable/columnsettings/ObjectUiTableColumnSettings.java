package net.disy.commons.swing.smarttable.columnsettings;

import javax.swing.Icon;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.smarttable.IObjectSelectionStrategy;
import net.disy.commons.swing.smarttable.ITableColumnViewSettings;
import net.disy.commons.swing.ui.IObjectUi;
import net.disy.commons.swing.ui.ObjectUiTableCellRenderer;

public class ObjectUiTableColumnSettings<T> implements ITableColumnViewSettings<T> {
   private final IObjectUi<T> ui;
   private final TableCellEditor editor;
   private int preferredWidth = 100;
   private int alignment;
   private boolean showText = true;

   public ObjectUiTableColumnSettings(IObjectUi<T> ui) {
      this(ui, null);
   }

   public ObjectUiTableColumnSettings(IObjectUi<T> ui, int alignment) {
      this(ui, null, alignment);
   }

   public ObjectUiTableColumnSettings(IObjectUi<T> ui, TableCellEditor editor) {
      this(ui, editor, 2);
   }

   public ObjectUiTableColumnSettings(IObjectUi<T> ui, TableCellEditor editor, int alignment) {
      Ensure.ensureArgumentNotNull(ui);
      this.ui = ui;
      this.editor = editor;
      this.alignment = alignment;
   }

   @Override
   public TableCellEditor getEditor() {
      return this.editor;
   }

   @Override
   public TableCellRenderer getRenderer() {
      return new ObjectUiTableCellRenderer<>(this.ui, this.alignment, this.showText);
   }

   @Override
   public boolean isResizable() {
      return true;
   }

   @Override
   public int getPreferredWidth() {
      return this.preferredWidth;
   }

   public void setPreferredWidth(int preferredWidth) {
      this.preferredWidth = preferredWidth;
   }

   public void setShowText(boolean showText) {
      this.showText = showText;
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
}
