package de.jave.jave;

import de.jave.jave.preferences.ColorScheme;
import de.jave.lib.CharacterPlate;
import de.jave.undo.UndoManager;
import de.jave.util.RecentFileList;
import de.jave.util.RelativeTimeClock;
import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import net.disy.commons.core.io.IOUtilities;

public class PlateDocument {
   private boolean modified;
   private UndoManager undoManager;
   private RelativeTimeClock clock;
   private File file;
   private CharacterPlate content;
   private Point scrollOrigin;
   private final Selection selection;
   private final Point cursorLocation;
   private ColorScheme colorScheme;
   private List<DocumentListener> documentListeners;

   private PlateDocument(Dimension size, ColorScheme colorScheme) {
      this.content = new CharacterPlate(size);
      this.selection = new Selection();
      this.cursorLocation = new Point(0, 0);
      this.colorScheme = colorScheme;
   }

   public void setFile(File file) {
      this.file = file;
   }

   public CharacterPlate getContent() {
      return this.content;
   }

   public void setContent(CharacterPlate cp) {
      this.content = cp;
   }

   public Point getCursorLocation() {
      return this.cursorLocation;
   }

   public void setColorScheme(ColorScheme colorScheme) {
      this.colorScheme = colorScheme;
   }

   public ColorScheme getColorScheme() {
      return this.colorScheme;
   }

   public synchronized void setDocumentState(CompressedDocumentState state) {
      this.content.setContent(state.getContent());
      this.selection.set(state.getSelectionLocation(), state.getSelectionContent(), state.getSelectionMask());
      this.scrollOrigin = state.getScrollOrigin();
      this.colorScheme = state.getColorScheme();
   }

   public void setRelativeTimeClock(RelativeTimeClock clock) {
      this.clock = clock;
   }

   public RelativeTimeClock getRelativeTimeClock() {
      return this.clock;
   }

   public boolean isEmpty() {
      return this.content == null || this.content.isEmpty();
   }

   public Selection getSelection() {
      return this.selection;
   }

   public void setScrollOrigin(Point p) {
      this.scrollOrigin = p;
   }

   public Dimension getSize() {
      return this.content.getSize();
   }

   public Point getScrollOrigin() {
      return this.scrollOrigin != null ? this.scrollOrigin : new Point(0, 0);
   }

   public static PlateDocument createNew(Dimension size, ColorScheme colorScheme) {
      PlateDocument doc = new PlateDocument(size, colorScheme);
      doc.modified = false;
      return doc;
   }

   public static PlateDocument load(File file, ColorScheme colorScheme) throws IOException {
      PlateDocument doc = new PlateDocument(new Dimension(1, 1), colorScheme);
      doc.file = file;
      doc.modified = false;
      String[] lines = readAsciiFileLines(file);
      doc.content = new CharacterPlate(lines);
      return doc;
   }

   public static String[] readAsciiFileLines(File file) throws IOException {
      BufferedReader reader = null;

      String[] lineStrings;
      try {
         reader = new BufferedReader(new FileReader(file));
         List<String> lines = new ArrayList<>();

         String s;
         while ((s = reader.readLine()) != null) {
            lines.add(s);
         }

         lineStrings = lines.toArray(new String[0]);
      } catch (IOException var8) {
         throw var8;
      } finally {
         IOUtilities.close(reader);
      }

      return lineStrings;
   }

   public void save(File file, RecentFileList recentFileList) throws IOException {
      if (file == null) {
         throw new RuntimeException("No name specified  for Document.save()!");
      } else {
         this.saveInternal(file, recentFileList);
         this.file = file;
         this.setModified(false);
      }
   }

   public void save(RecentFileList recentFileList) throws IOException {
      if (this.file == null) {
         throw new RuntimeException("No name specified  for Document.save()!");
      } else {
         this.saveInternal(this.file, recentFileList);
         this.setModified(false);
      }
   }

   protected void saveInternal(File file, RecentFileList recentFileList) throws IOException {
      BufferedWriter bw = null;

      try {
         bw = new BufferedWriter(new FileWriter(file));
         String[] lines = this.content.toStringArray();

         for (int i = 0; i < lines.length - 1; i++) {
            bw.write(lines[i]);
            bw.newLine();
         }

         bw.write(lines[lines.length - 1]);
         bw.close();
         recentFileList.add(file);
      } catch (IOException var9) {
         throw var9;
      } finally {
         if (bw != null) {
            bw.close();
         }
      }
   }

   public boolean isModified() {
      return this.modified;
   }

   public void setModified(boolean what) {
      this.modified = what;
   }

   public boolean hasFile() {
      return this.file != null;
   }

   public boolean isLogging() {
      return this.undoManager.isLogging();
   }

   public String getLogFileName() {
      String result = this.undoManager.getLogFileName();
      return result == null ? "" : result;
   }

   public File getFile() {
      return !this.hasFile() ? null : this.file;
   }

   public synchronized void addDocumentListener(DocumentListener l) {
      if (this.documentListeners == null) {
         this.documentListeners = new Vector<>(2, 2);
      }

      this.documentListeners.add(l);
   }

   public synchronized void removeDocumentListener(DocumentListener l) {
      this.documentListeners.remove(l);
   }

   public void documentClosing() {
      if (this.documentListeners != null) {
         for (int i = 0; i < this.documentListeners.size(); i++) {
            this.documentListeners.get(i).documentClosing();
         }
      }
   }

   public void documentHiding() {
      if (this.documentListeners != null) {
         for (int i = 0; i < this.documentListeners.size(); i++) {
            this.documentListeners.get(i).documentHiding();
         }
      }
   }

   public void documentShowing() {
      if (this.documentListeners != null) {
         for (int i = 0; i < this.documentListeners.size(); i++) {
            this.documentListeners.get(i).documentShowing();
         }
      }
   }

   public void documentChanged() {
      if (this.documentListeners != null) {
         for (int i = 0; i < this.documentListeners.size(); i++) {
            this.documentListeners.get(i).documentChanged();
         }
      }
   }

   public UndoManager getUndoManager() {
      return this.undoManager;
   }

   public void setUndoManager(UndoManager manager) {
      this.undoManager = manager;
   }
}
