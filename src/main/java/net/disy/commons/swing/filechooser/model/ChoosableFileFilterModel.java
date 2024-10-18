package net.disy.commons.swing.filechooser.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.util.Ensure;

public class ChoosableFileFilterModel extends AbstractChangeableModel {
   public static final FileFilter ACCEPT_ALL_FILE_FILTER = new FileFilter() {
      @Override
      public String getDescription() {
         return UIManager.getString("FileChooser.acceptAllFileFilterText");
      }

      @Override
      public boolean accept(File f) {
         return true;
      }
   };
   final List<FileFilter> fileFilters = new ArrayList<>();

   public void addFileFilter(FileFilter filter) {
      Ensure.ensureArgumentNotNull(filter);
      if (!this.fileFilters.contains(filter)) {
         this.fileFilters.add(filter);
         this.fireChangeEvent();
      }
   }

   public FileFilter[] getFileFilters() {
      return this.fileFilters.toArray(new FileFilter[0]);
   }

   public void clear() {
      this.fileFilters.clear();
      this.fireChangeEvent();
   }

   public void removeFileFilter(FileFilter fileFilter) {
      if (this.fileFilters.remove(fileFilter)) {
         this.fireChangeEvent();
      }
   }

   public boolean isEmpty() {
      return this.fileFilters.isEmpty();
   }
}
