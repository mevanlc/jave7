package net.disy.commons.swing.tree;

public interface ISmartTree<T> {
   T getRoot();

   T getChild(T var1, int var2);

   int getChildCount(T var1);

   int getIndexOfChild(T var1, T var2);

   boolean isLeaf(T var1);

   Class<T> getNodeClass();
}
