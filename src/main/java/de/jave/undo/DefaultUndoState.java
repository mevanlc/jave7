package de.jave.undo;

public class DefaultUndoState implements UndoState {
   protected Object object;

   public DefaultUndoState(Object object) {
      this.object = object;
   }

   @Override
   public int getEstimatedMemorySize() {
      return 1;
   }

   @Override
   public String getUndoActionName() {
      return this.object.toString();
   }

   @Override
   public String toString() {
      return this.object == null ? "null" : this.object.toString();
   }
}
