package net.disy.commons.swing.smarttable;

import java.awt.Component;
import net.disy.commons.core.util.IClosure;

public interface IObjectSelectionStrategy<T> {
   void invokeForValue(Component var1, T var2, IClosure<T> var3);
}
