package de.jave.util;

import de.jave.lib.gui.GuiUtilities;
import de.jave.preferences.SmartPreferences;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import net.dizzy.commons.core.io.FileDisplayNameUtilities;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;

public class RecentFileList implements ActionListener {
   private static final String KEY_MAX_FILE_COUNT = "maxFileCount";
   private static final String KEY_FILE_COUNT = "fileCount";
   private static final String KEY_PREFIX_FILE = "file";
   private final LinkedList files = new LinkedList();
   private JMenu menu;
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
         File file = new File(fileName);
         this.files.add(file);
      }
   }

   public void setRecentFileOpenListener(RecentFileOpenListener listener) {
      this.listener = listener;
   }

   public void add(File file) {
      this.delete(file);
      this.files.addFirst(file);
      if (this.files.size() > this.maxSize) {
         this.files.removeLast();
      }

      this.updateMenu();
   }

   public void delete(File file) {
      this.files.remove(file);
      this.updateMenu();
   }

   public void setMaxSize(int maxSize) {
      if (maxSize > 9) {
         maxSize = 9;
      }

      while (this.files.size() > maxSize) {
         this.files.removeLast();
      }

      this.maxSize = maxSize;
   }

   public int getMaxSize() {
      return this.maxSize;
   }

   public void check() {
      Iterator iter = this.files.iterator();

      while (iter.hasNext()) {
         File file = (File)iter.next();
         if (!file.exists()) {
            iter.remove();
         }
      }
   }

   public File[] getFiles() {
      return (File[]) this.files.toArray(new File[0]);
   }

   public int getSize() {
      return this.files.size();
   }

   public File getFile(int index) {
      return (File)this.files.get(index);
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("-- ").append(this.files.size()).append(" Eintäge in der History\n");

      for (int i = 0; i < this.files.size(); i++) {
         File file = (File)this.files.get(i);
         sb.append(i).append(" ").append(file.getAbsolutePath()).append("\n");
      }

      return sb.toString();
   }

   public void print() {
      System.out.print(this.toString());
   }

   public void setMenu(JMenu menu) {
      if (this.menu != null) {
         this.menu.removeAll();
      }

      this.menu = menu;
      this.updateMenu();
   }

   private void updateMenu() {
      if (this.menu != null) {
         this.menu.removeAll();
         File[] f = this.getFiles();
         if (f.length == 0) {
            JMenuItem m = new JMenuItem("none");
            m.setEnabled(false);
            this.menu.add(m);
         } else {
            for (int i = 0; i < f.length; i++) {
               String displayFileName = FileDisplayNameUtilities.createShortenedFileName(f[i].getPath(), 25);
               JMenuItem mi = new JMenuItem(i + " " + displayFileName);
               mi.addActionListener(this);
               this.menu.add(mi);
            }
         }
      }
   }

   @Override
   public void actionPerformed(ActionEvent evt) {
      if (this.listener != null) {
         Component parentComponent = GuiUtilities.getWindowForComponent(evt);
         JMenuItem mi = (JMenuItem)evt.getSource();
         int menuSize = this.menu.getMenuComponentCount();

         for (int i = 0; i < menuSize; i++) {
            if (this.menu.getMenuComponent(i) == mi) {
               File file = this.getFile(i);
               if (!file.exists()) {
                  MessageDialogFactory.showMessageDialog(
                     parentComponent, new Message("The file '" + file.getAbsolutePath() + "' does not exist.", MessageType.ERROR)
                  );
                  this.delete(file);
                  return;
               }

               this.listener.openRecentFile(parentComponent, file);
            }
         }
      }
   }

   public void flush() {
      this.preferences.put("maxFileCount", this.maxSize);
      this.preferences.put("fileCount", this.files.size());

      for (int i = 0; i < this.files.size(); i++) {
         File file = (File)this.files.get(i);
         this.preferences.put("file" + i, file.getAbsolutePath());
      }

      this.preferences.flush();
   }
}
