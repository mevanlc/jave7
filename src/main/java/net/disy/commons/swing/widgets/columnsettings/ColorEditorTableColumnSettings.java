package net.disy.commons.swing.smarttable.columnsettings;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.swing.smarttable.IObjectSelectionStrategy;
import net.disy.commons.swing.smarttable.ITableColumnViewSettings;
import net.disy.commons.swing.smarttable.celleditors.ColorCellEditor;

public class ColorEditorTableColumnSettings implements ITableColumnViewSettings<Color> {
   @Override
   public TableCellEditor getEditor() {
      return new ColorCellEditor(false);
   }

   @Override
   public TableCellRenderer getRenderer() {
      return new ColorCellEditor(false);
   }

   @Override
   public int getPreferredWidth() {
      return 40;
   }

   @Override
   public boolean isResizable() {
      return true;
   }

   @Override
   public IObjectSelectionStrategy<Color> getDoubleClickBehaviour() {
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
