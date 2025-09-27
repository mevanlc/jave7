package net.disy.commons.swing.tree;

public interface ISmartTreeRenameStrategy<T> {
   boolean isRenameable(T var1);

   String getOriginalName(T var1);

   boolean isValidNewName(T var1, String var2);

   void renameTo(T var1, String var2);
}
