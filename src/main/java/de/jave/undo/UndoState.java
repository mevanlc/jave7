package de.jave.undo;

public interface UndoState {
   int getEstimatedMemorySize();

   String getUndoActionName();
}
