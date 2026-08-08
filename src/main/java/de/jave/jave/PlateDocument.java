package de.jave.jave;

import de.jave.gui.io.FileExtension;
import de.jave.gui.io.FileExtensions;
import de.jave.jave.layers.ActiveLayerCharacterPlate;
import de.jave.jave.layers.JaveDocArchive;
import de.jave.jave.layers.LayeredDocument;
import de.jave.jave.layers.SecondaryLayer;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Vector;
import net.dizzy.commons.core.io.IOUtilities;

public class PlateDocument {
   private boolean modified;
   private UndoManager undoManager;
   private RelativeTimeClock clock;
   private File file;
   private CharacterPlate content;
   private LayeredDocument layeredDocument;
   private CharacterPlate activeLayerContent;
   private Point scrollOrigin;
   private final Selection selection;
   private final Point cursorLocation;
   private ColorScheme colorScheme;
   private List<DocumentListener> documentListeners;

   private PlateDocument(Dimension size, ColorScheme colorScheme) {
      this.setContent(new CharacterPlate(size));
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

   public CharacterPlate getEditableContent() {
      return this.activeLayerContent;
   }

   public CharacterPlate getCompositeContent() {
      return this.layeredDocument.getComposite(false);
   }

   public void setContent(CharacterPlate cp) {
      this.content = cp;
      if (this.layeredDocument == null) {
         this.layeredDocument = LayeredDocument.fromContent(cp);
      } else {
         this.layeredDocument.getDocumentLayer().setContent(cp);
      }
      this.activeLayerContent = new ActiveLayerCharacterPlate(this.layeredDocument);
   }

   public LayeredDocument getLayeredDocument() {
      return this.layeredDocument;
   }

   public boolean hasSecondaryLayers() {
      return this.layeredDocument != null && this.layeredDocument.getLayerCount() > 1;
   }

   public int getLayerCount() {
      return this.layeredDocument.getLayerCount();
   }

   public int getActiveLayerNumber() {
      return this.layeredDocument.getActiveLayerNumber();
   }

   public void activateLayerNumber(int layerNumber) {
      this.layeredDocument.activateLayerNumber(layerNumber);
   }

   public void addSecondaryLayerAboveActive() {
      this.layeredDocument.addSecondaryLayerAboveActive();
   }

   public boolean renameLayer(String layerId, String name) {
      return this.layeredDocument.renameLayer(layerId, name);
   }

   public void duplicateActiveLayer() {
      this.layeredDocument.duplicateActiveLayer();
   }

   public boolean canDeleteActiveLayer() {
      return this.layeredDocument.canDeleteActiveLayer();
   }

   public boolean deleteActiveLayer() {
      return this.layeredDocument.deleteActiveLayer();
   }

   public boolean canMoveActiveLayerUp() {
      return this.layeredDocument.canMoveActiveLayerUp();
   }

   public boolean moveActiveLayerUp() {
      return this.layeredDocument.moveActiveLayerUp();
   }

   public boolean canMoveActiveLayerDown() {
      return this.layeredDocument.canMoveActiveLayerDown();
   }

   public boolean moveActiveLayerDown() {
      return this.layeredDocument.moveActiveLayerDown();
   }

   public boolean canToggleActiveLayerVisibility() {
      return this.layeredDocument.canToggleActiveLayerVisibility();
   }

   public boolean isActiveLayerVisible() {
      return this.layeredDocument.isActiveLayerVisible();
   }

   public boolean toggleActiveLayerVisibility() {
      return this.layeredDocument.toggleActiveLayerVisibility();
   }

   public boolean canToggleActiveLayerOpacity() {
      return this.layeredDocument.canToggleActiveLayerOpacity();
   }

   public boolean isActiveLayerOpaque() {
      return this.layeredDocument.isActiveLayerOpaque();
   }

   public boolean setActiveLayerOpaque(boolean opaque) {
      return this.layeredDocument.setActiveLayerOpaque(opaque);
   }

   public void flattenLayers(boolean includeHiddenSecondaryLayers) {
      this.layeredDocument.flatten(includeHiddenSecondaryLayers);
      this.content = this.layeredDocument.getDocumentLayer().getContent();
      this.activeLayerContent = new ActiveLayerCharacterPlate(this.layeredDocument);
   }

   public void resizeDocument(int width, int height) {
      this.layeredDocument.resizeDocument(width, height);
      this.content = this.layeredDocument.getDocumentLayer().getContent();
   }

   public void activateNextLayer() {
      this.layeredDocument.activateNextLayer();
   }

   public SecondaryLayer getActiveSecondaryLayer() {
      return this.layeredDocument.getActiveSecondaryLayer();
   }

   public void activateDocumentLayer() {
      this.layeredDocument.activateDocumentLayer();
   }

   public void activateFirstSecondaryLayerOrCreate() {
      SecondaryLayer layer = this.layeredDocument.activateFirstSecondaryLayerOrCreate();
      layer.setName("Layer 2");
   }

   public boolean isDocumentLayerActive() {
      return this.layeredDocument.isDocumentLayerActive();
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
      this.setContent(new CharacterPlate(state.getContent()));
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
      return this.layeredDocument == null || this.layeredDocument.getComposite(false).isEmpty();
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
      if (isJaveDocFile(file)) {
         doc.layeredDocument = JaveDocArchive.read(file);
         doc.content = doc.layeredDocument.getDocumentLayer().getContent();
         doc.activeLayerContent = new ActiveLayerCharacterPlate(doc.layeredDocument);
      } else {
         String[] lines = readAsciiFileLines(file);
         doc.setContent(new CharacterPlate(lines));
      }
      return doc;
   }

   public static String[] readAsciiFileLines(File file) throws IOException {
      BufferedReader reader = null;

      String[] lineStrings;
      try {
         reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
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
      if (isJaveDocFile(file)) {
         JaveDocArchive.write(this.layeredDocument, file);
         recentFileList.add(file);
         return;
      }
      BufferedWriter bw = null;

      try {
         bw = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);
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

   public boolean isJaveDocBacked() {
      return this.file != null && isJaveDocFile(this.file);
   }

   private static boolean isJaveDocFile(File file) {
      return FileExtensions.JAVEDOC.equals(FileExtension.getFrom(file));
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
