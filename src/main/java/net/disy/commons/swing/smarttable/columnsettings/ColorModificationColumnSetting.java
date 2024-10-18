package net.disy.commons.swing.smarttable.columnsettings;

import java.awt.Color;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.swing.smarttable.celleditors.ColorModificationCellEditor;
import net.disy.commons.swing.smarttable.columnsettings.preferredwidth.IPreferredWidth;

public final class ColorModificationColumnSetting extends AbstractTableColumnSettings<Color> {
   public ColorModificationColumnSetting(IPreferredWidth preferredWidth) {
      super(preferredWidth);
   }

   @Override
   public TableCellEditor getEditor() {
      return new ColorModificationCellEditor();
   }

   @Override
   protected TableCellRenderer getBaseRenderer() {
      return new ColorModificationCellEditor();
   }
}
