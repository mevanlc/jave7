package net.disy.commons.swing.tree;

import java.awt.Component;

public interface ISmartTreeDeleteStrategy<T> {
   boolean isDeletable(T var1);

   boolean deleteNode(Component var1, T var2, int var3, T var4);
}
