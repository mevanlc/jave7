package net.disy.commons.swing.smarttable.columnsettings;

import java.awt.Component;
import net.disy.commons.core.util.IClosure;
import net.disy.commons.swing.smarttable.IObjectSelectionStrategy;

public class NullDoubleClickBehaviour<T> implements IObjectSelectionStrategy<T> {
   @Override
   public void invokeForValue(Component parentComponent, T cellValue, IClosure<T> resultReceiver) {
      resultReceiver.execute(cellValue);
   }
}
