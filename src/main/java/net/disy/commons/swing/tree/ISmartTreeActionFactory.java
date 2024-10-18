package net.disy.commons.swing.tree;

import net.disy.commons.swing.action.GroupedMenuItem;

public interface ISmartTreeActionFactory<T> {
   GroupedMenuItem createMenuItem(T[] var1);

   boolean supportsPath(T[] var1);
}
