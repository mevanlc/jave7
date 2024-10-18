package de.jave.undo;

class UndoItem {
   public UndoState undoState;
   public UndoItem next;
   public UndoItem prev;

   public UndoItem(UndoState undoState) {
      this.undoState = undoState;
      this.next = null;
      this.prev = null;
   }

   public int getEstimatedMemorySize() {
      return this.undoState.getEstimatedMemorySize();
   }

   public String getUndoActionName() {
      return this.undoState.getUndoActionName();
   }

   public UndoState getUndoState() {
      return this.undoState;
   }

   @Override
   public String toString() {
      String p = "|-";
      if (this.prev != null) {
         p = "<-";
      }

      String n = "-|";
      if (this.next != null) {
         n = "->";
      }

      StringBuffer result = new StringBuffer();
      result.append(p);
      result.append(this.undoState.getUndoActionName()).append("_");
      result.append("ca._").append(this.undoState.getEstimatedMemorySize()).append("Byte_");
      result.append("[").append(this.undoState).append("]");
      result.append(n);
      return result.toString();
   }

   public void print() {
      System.err.println(this.toString());
   }
}
