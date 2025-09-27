package net.disy.commons.swing.smarttable.columnsettings;

import java.awt.Color;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.swing.smarttable.celleditors.StringCellEditor;
import net.disy.commons.swing.smarttable.cellrenderers.StringCellRenderer;

public class StringTableColumnSettings extends AbstractTableColumnSettings<String> {
   private final TableCellRenderer renderer;

   public StringTableColumnSettings() {
      this(8, new StringCellRenderer());
   }

   public StringTableColumnSettings(int preferredColumnCount) {
      this(preferredColumnCount, new StringCellRenderer());
   }

   public StringTableColumnSettings(int preferredColumnCount, boolean showTooltip) {
      this(preferredColumnCount, new StringCellRenderer(showTooltip));
   }

   public StringTableColumnSettings(Color background) {
      this(8, new StringCellRenderer(), background);
   }

   public StringTableColumnSettings(TableCellRenderer renderer) {
      this(8, renderer);
   }

   public StringTableColumnSettings(int preferredColumnCount, TableCellRenderer renderer) {
      this(preferredColumnCount, renderer, null);
   }

   public StringTableColumnSettings(int preferredColumnCount, TableCellRenderer renderer, Color background) {
      super(preferredColumnCount, background);
      this.renderer = renderer;
   }

   @Override
   public TableCellEditor getEditor() {
      return new StringCellEditor();
   }

   @Override
   protected TableCellRenderer getBaseRenderer() {
      return this.renderer;
   }
}
