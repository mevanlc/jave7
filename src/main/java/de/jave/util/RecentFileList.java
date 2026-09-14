package de.jave.util;

import de.jave.preferences.SmartPreferences;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;

public class RecentFileList {
   private static final String KEY_MAX_FILE_COUNT = "maxFileCount";
   private static final String KEY_FILE_COUNT = "fileCount";
   private static final String KEY_PREFIX_FILE = "file";
   private final LinkedList<File> files = new LinkedList<>();
   private final List<Runnable> listeners = new ArrayList<>();
   private RecentFileOpenListener listener;
   private int maxSize;
   private final SmartPreferences preferences;

   public RecentFileList(SmartPreferences preferences) {
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
      this.maxSize = preferences.getInt("maxFileCount", 5);
      int fileCount = preferences.getInt("fileCount", 0);

      for (int i = 0; i < fileCount; i++) {
         String fileName = preferences.get("file" + i, null);
         File file = normalize(new File(fileName));
         if (!this.files.contains(file)) {
            this.files.add(file);
         }
      }
   }

   private static File normalize(File file) {
      return file.toPath().toAbsolutePath().normalize().toFile();
   }

   public void setRecentFileOpenListener(RecentFileOpenListener listener) {
      this.listener = listener;
   }

   public void add(File file) {
      file = normalize(file);
      this.files.remove(file);
      this.files.addFirst(file);
      if (this.files.size() > this.maxSize) {
         this.files.removeLast();
      }

      this.fireChanged();
   }

   public void delete(File file) {
      if (this.files.remove(normalize(file))) {
         this.fireChanged();
      }
   }

   public void setMaxSize(int maxSize) {
      if (maxSize > 9) {
         maxSize = 9;
      }

      while (this.files.size() > maxSize) {
         this.files.removeLast();
      }

      this.maxSize = maxSize;
      this.fireChanged();
   }

   public int getMaxSize() {
      return this.maxSize;
   }

   public void check() {
      if (this.files.removeIf(file -> !file.exists())) {
         this.fireChanged();
      }
   }

   public File[] getFiles() {
      return this.files.toArray(new File[0]);
   }

   public int getSize() {
      return this.files.size();
   }

   public File getFile(int index) {
      return this.files.get(index);
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("-- ").append(this.files.size()).append(" Eintäge in der History\n");

      for (int i = 0; i < this.files.size(); i++) {
         File file = this.files.get(i);
         sb.append(i).append(" ").append(file.getAbsolutePath()).append("\n");
      }

      return sb.toString();
   }

   public void print() {
      System.out.print(this.toString());
   }

   public void addChangeListener(Runnable listener) {
      this.listeners.add(listener);
   }

   private void fireChanged() {
      List.copyOf(this.listeners).forEach(Runnable::run);
   }

   public void open(File file, Component parentComponent) {
      if (this.listener == null) {
         return;
      }
      if (!file.exists()) {
         MessageDialogFactory.showMessageDialog(
            parentComponent, new Message("The file '" + file.getAbsolutePath() + "' does not exist.", MessageType.ERROR)
         );
         this.delete(file);
         return;
      }
      this.listener.openRecentFile(parentComponent, file);
   }

   public void flush() {
      this.preferences.put("maxFileCount", this.maxSize);
      this.preferences.put("fileCount", this.files.size());

      for (int i = 0; i < this.files.size(); i++) {
         File file = this.files.get(i);
         this.preferences.put("file" + i, file.getAbsolutePath());
      }

      this.preferences.flush();
   }
}
