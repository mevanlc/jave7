package net.disy.commons.swing.tree;

public interface ISmartTreeMoveStrategy<T> {
   boolean isMovableTreeNode(T var1);

   void handleNodeMoved(T var1, T var2, T var3, int var4);
}
