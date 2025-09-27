package net.disy.commons.swing.smarttable.columnsettings;

import javax.swing.Icon;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.smarttable.IObjectSelectionStrategy;
import net.disy.commons.swing.smarttable.ITableColumnViewSettings;
import net.disy.commons.swing.smarttable.celleditors.ButtonCellEditor;

public final class ButtonTableColumnSettings implements ITableColumnViewSettings<Object> {
   private final IButtonEditorConfiguration configuration;

   public ButtonTableColumnSettings(IButtonEditorConfiguration configuration) {
      Ensure.ensureArgumentNotNull(configuration);
      this.configuration = configuration;
   }

   @Override
   public TableCellEditor getEditor() {
      return new ButtonCellEditor(this.configuration);
   }

   @Override
   public TableCellRenderer getRenderer() {
      return new ButtonCellEditor(this.configuration);
   }

   @Override
   public boolean isResizable() {
      return false;
   }

   @Override
   public int getPreferredWidth() {
      return ButtonCellEditor.getButtonWidth(this.configuration.getLongestButtonLabel(), this.configuration.getLargestButtonIcon());
   }

   @Override
   public IObjectSelectionStrategy<Object> getDoubleClickBehaviour() {
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
