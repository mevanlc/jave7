package de.jave.undo;

import java.io.File;
import java.io.IOException;

public final class LogFile {
   private final File file = LogFileManager.getInstance().createNewLogFile();

   public LogFile() throws IOException {
   }

   public String getAbsolutePath() {
      return this.file.getAbsolutePath();
   }

   public void append(UndoState o) {
      LogFileManager.getInstance().enqueue(new LogFileManagerEntry(this.file, o));
   }
}
