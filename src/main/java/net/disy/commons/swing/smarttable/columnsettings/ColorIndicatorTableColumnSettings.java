package net.disy.commons.swing.smarttable.columnsettings;

import java.awt.Color;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.swing.smarttable.celleditors.ColorCellEditor;
import net.disy.commons.swing.smarttable.columnsettings.preferredwidth.TextPreferredWidth;

public class ColorIndicatorTableColumnSettings extends AbstractTableColumnSettings<Color> {
   public ColorIndicatorTableColumnSettings() {
      super(new TextPreferredWidth(3));
   }

   @Override
   public TableCellEditor getEditor() {
      return new ColorCellEditor(true);
   }

   @Override
   protected TableCellRenderer getBaseRenderer() {
      return new ColorCellEditor(true);
   }
}
