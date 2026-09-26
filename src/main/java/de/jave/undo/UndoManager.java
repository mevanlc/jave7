package de.jave.undo;

public class UndoManager {
   private UndoItem currentItem;
   private UndoItem firstItem;
   private boolean truncated;
   private int maxSize;
   private LogFile logFile;

   @Deprecated
   public UndoManager(Object initialState) {
      this(new DefaultUndoState(initialState));
   }

   public UndoManager(UndoState initialState) {
      this.currentItem = new UndoItem(initialState);
      this.firstItem = this.currentItem;
      this.truncated = false;
      this.logFile = null;
   }

   public void setLogFile(LogFile logFile) {
      this.logFile = logFile;
   }

   public LogFile getLogFile() {
      return this.logFile;
   }

   public String getLogFileName() {
      return this.logFile == null ? null : this.logFile.getAbsolutePath();
   }

   public boolean isLogging() {
      return this.logFile != null;
   }

   @Deprecated
   public synchronized void saveCurrentState(Object o) {
      this.saveCurrentState(new DefaultUndoState(o));
   }

   public synchronized void saveCurrentState(UndoState o) {
      UndoItem item = new UndoItem(o);
      this.currentItem.next = item;
      item.prev = this.currentItem;
      this.currentItem = item;
      this.ensureMaxSize();
      if (this.logFile != null) {
         this.logFile.append(o);
      }
   }

   public synchronized boolean canUndo() {
      return this.currentItem.prev != null;
   }

   public synchronized boolean isModified() {
      return this.truncated || this.currentItem.prev == null;
   }

   public synchronized UndoState undo() {
      this.currentItem = this.currentItem.prev;
      return this.currentItem.getUndoState();
   }

   public synchronized UndoState getNextState() {
      return this.currentItem.next == null ? null : this.currentItem.next.getUndoState();
   }

   public synchronized UndoState getCurrentState() {
      return this.currentItem.getUndoState();
   }

   public synchronized boolean canRedo() {
      return this.currentItem.next != null;
   }

   public synchronized UndoState redo() {
      this.currentItem = this.currentItem.next;
      return this.currentItem.getUndoState();
   }

   public synchronized String getUndoActionName() {
      String an = this.currentItem.getUndoActionName();
      return an != null ? an : "";
   }

   public synchronized String getRedoActionName() {
      UndoItem nextItem = this.currentItem.next;
      if (nextItem == null) {
         return "";
      } else {
         String an = nextItem.getUndoActionName();
         return an != null ? an : "";
      }
   }

   public void setMaxSize(int maxSize) {
      this.maxSize = maxSize;
      this.ensureMaxSize();
   }

   protected synchronized void ensureMaxSize() {
      if (this.maxSize > 0) {
         UndoItem item = this.firstItem;

         int size;
         for (size = 0; item != null; item = item.next) {
            size += item.getEstimatedMemorySize();
         }

         if (size > this.maxSize) {
            while (size > this.maxSize && this.currentItem != this.firstItem.next) {
               this.truncated = true;
               size -= this.firstItem.getEstimatedMemorySize();
               this.firstItem = this.firstItem.next;
               if (this.firstItem != null) {
                  this.firstItem.prev = null;
               }
            }
         }
      }
   }

   public void print() {
      System.out.println("-------------");
      if (this.currentItem == null) {
         System.out.println("LEER");
      } else {
         if (this.truncated) {
            System.out.println("*truncated*");
         }

         UndoItem item = this.firstItem;

         int size;
         for (size = 0; item != null; item = item.next) {
            if (item == this.currentItem) {
               System.out.print("* ");
            } else {
               System.out.print("  ");
            }

            item.print();
            size += item.getEstimatedMemorySize();
         }

         if (size > 0) {
            System.err.println(" => " + size + " Bytes");
         }
      }
   }

   public static void main(String[] args) {
      String s = "";
      UndoManager man = new UndoManager(s);
      man.print();
      s = s + "A";
      man.saveCurrentState(s);
      s = s + "B";
      man.saveCurrentState(s);
      s = s + "C";
      man.saveCurrentState(s);
      s = man.undo().toString();
      s = man.undo().toString();
      s = s + "D";
      man.saveCurrentState(s);
   }
}
