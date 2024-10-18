package net.disy.commons.swing.undo;

public interface IUndoStateManager<T> {
   void setState(T var1);

   T getState();
}
