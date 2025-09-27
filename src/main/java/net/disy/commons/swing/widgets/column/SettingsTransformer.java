package net.disy.commons.swing.smarttable.column;

import net.disy.commons.core.util.ITransformer;
import net.disy.commons.swing.smarttable.ITableColumnViewSettings;

public final class SettingsTransformer implements ITransformer<ITableColumn<?>, ITableColumnViewSettings<?>> {
   public ITableColumnViewSettings<?> transform(ITableColumn<?> input) {
      return input.getSettings();
   }
}
