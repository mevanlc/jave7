package de.jave.jave;

import de.jave.jave.preferences.ColorScheme;
import de.jave.util.RecentFileList;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.util.Ensure;

public class DocumentManager {
   private final List<PlateDocument> documents = new ArrayList<>();
   private int currentDocumentIndex;
   private final FileModel currentDirectoryModel;
   private final ObjectModel<ColorScheme> defaultColorSchemeModel;

   public DocumentManager(FileModel currentDirectoryModel, ObjectModel<ColorScheme> defaultColorSchemeModel) {
      Ensure.ensureArgumentNotNull(defaultColorSchemeModel);
      this.currentDirectoryModel = currentDirectoryModel;
      this.defaultColorSchemeModel = defaultColorSchemeModel;
      this.currentDocumentIndex = -1;
   }

   public synchronized void setCurrentDocument(int index) {
      this.currentDocumentIndex = index;
   }

   public int getIndexOf(PlateDocument doc) {
      return this.documents.indexOf(doc);
   }

   public int getSize() {
      return this.documents.size();
   }

   public synchronized PlateDocument createNew(Dimension size) {
      PlateDocument doc = PlateDocument.createNew(size, this.defaultColorSchemeModel.getValue());
      this.documents.add(doc);
      this.currentDocumentIndex = this.documents.size() - 1;
      return doc;
   }

   public synchronized PlateDocument load(File file, RecentFileList recentFileList) throws IOException {
      PlateDocument doc = null;

      try {
         doc = PlateDocument.load(file, this.defaultColorSchemeModel.getValue());
      } catch (OutOfMemoryError var5) {
         throw var5;
      }

      recentFileList.add(file);
      this.getCurrentDirectoryModel().setValue(file.getParentFile());
      this.documents.add(doc);
      this.currentDocumentIndex = this.documents.size() - 1;
      return doc;
   }

   public boolean isAlreadyOpen(File file) {
      for (int i = 0; i < this.documents.size(); i++) {
         PlateDocument document = this.getDocument(i);
         if (file.equals(document.getFile())) {
            return true;
         }
      }

      return false;
   }

   public synchronized void closeCurrentDocument() {
      this.closeDocument(this.currentDocumentIndex);
   }

   public synchronized void closeDocument(int index) {
      PlateDocument doc = this.documents.get(index);
      doc.documentClosing();
      this.documents.remove(index);
      if (index <= this.currentDocumentIndex) {
         this.currentDocumentIndex--;
         if (this.currentDocumentIndex < 0 && this.getSize() > 0) {
            this.currentDocumentIndex = 0;
         }
      }
   }

   public int getCurrentDocumentIndex() {
      return this.currentDocumentIndex;
   }

   public PlateDocument getCurrentDocument() {
      return this.currentDocumentIndex < 0 ? null : this.getDocument(this.currentDocumentIndex);
   }

   public PlateDocument getDocument(int i) {
      return this.documents.get(i);
   }

   public synchronized String[] getDocumentStatusList() {
      String[] result = new String[this.documents.size() * 2];

      for (int i = 0; i < this.documents.size(); i++) {
         File documentFile = this.getDocument(i).getFile();
         result[i * 2] = documentFile == null ? "" : documentFile.getAbsolutePath();
         result[i * 2 + 1] = this.getDocument(i).getLogFileName();
      }

      return result;
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("DocumentManager(").append(this.getSize()).append(",").append(this.getCurrentDirectoryModel().getValue()).append("){\n");

      for (int i = 0; i < this.getSize(); i++) {
         sb.append("  ").append(this.documents.get(i)).append("\n");
      }

      sb.append("};\n");
      return sb.toString();
   }

   public void print() {
      System.out.println(this.toString());
   }

   public FileModel getCurrentDirectoryModel() {
      return this.currentDirectoryModel;
   }
}
