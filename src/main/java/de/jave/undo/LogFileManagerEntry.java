package de.jave.undo;

import java.io.File;
import net.disy.commons.core.util.Ensure;

public class LogFileManagerEntry {
   private final File file;
   private final UndoState undoState;

   public LogFileManagerEntry(File file, UndoState undoState) {
      Ensure.ensureArgumentNotNull(file);
      Ensure.ensureArgumentNotNull(undoState);
      this.file = file;
      this.undoState = undoState;
   }

   public File getFile() {
      return this.file;
   }

   public UndoState getUndoState() {
      return this.undoState;
   }
}
