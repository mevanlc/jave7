package de.jave.undo;

import de.jave.jave.JaveStatusFile;
import de.jave.util.Queue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import net.dizzy.commons.core.io.IOUtilities;

public class LogFileManager extends Thread {
   private static LogFileManager instance = new LogFileManager();
   private final Queue queue = new Queue();
   private final boolean shallStop = false;

   public static LogFileManager getInstance() {
      return instance;
   }

   public LogFileManager() {
      super("LogFileManager");
      this.setPriority(1);
      this.start();
   }

   @Override
   public void run() {
      while (true) {
         LogFileManagerEntry objectToWrite = null;
         synchronized (this.queue) {
            while (this.queue.isEmpty()) {
               try {
                  this.queue.wait();
               } catch (InterruptedException var5) {
               }
            }

            if (!this.queue.isEmpty()) {
               objectToWrite = (LogFileManagerEntry)this.queue.get();
            }
         }

         if (objectToWrite != null) {
            this.write(objectToWrite);
         }
      }
   }

   private void write(LogFileManagerEntry entry) {
      String s = entry.getUndoState().toString();
      BufferedWriter writer = null;

      try {
         writer = new BufferedWriter(new FileWriter(entry.getFile(), true));
         writer.write(s);
         writer.newLine();
      } catch (IOException var8) {
         System.err.println(var8);
         var8.printStackTrace();
      } finally {
         IOUtilities.close(writer);
      }
   }

   public File createNewLogFile() throws IOException {
      File logFileFolder = JaveStatusFile.logDir;
      String logFileFileExtension = ".jlog";
      synchronized (this) {
         if (!logFileFolder.exists()) {
            boolean success = logFileFolder.mkdirs();
         }

         int r = (int)(Math.random() * 2.147483647E9);

         File file;
         do {
            r = r < 0 ? -r : r;
            file = new File(logFileFolder, r + ".jlog");
            r++;
         } while (file.exists());

         file.createNewFile();
         return file;
      }
   }

   public void enqueue(LogFileManagerEntry entry) {
      synchronized (this.queue) {
         this.queue.put(entry);
         this.queue.notifyAll();
      }
   }
}
