package net.disy.commons.swing.tree;

public abstract class AbstractSmartTreeActionFactory<T> implements ISmartTreeActionFactory<T> {
   @Override
   public boolean supportsPath(T[] path) {
      return path != null;
   }
}
