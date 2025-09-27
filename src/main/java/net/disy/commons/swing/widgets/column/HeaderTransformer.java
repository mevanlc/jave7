package net.disy.commons.swing.smarttable.column;

import net.disy.commons.core.util.ITransformer;

public final class HeaderTransformer implements ITransformer<ITableColumn<?>, Object> {
   public Object transform(ITableColumn<?> input) {
      return input.getHeader();
   }
}
