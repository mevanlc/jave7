package de.jave.core.io;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.disy.commons.core.model.AbstractChangeableModel;

public class FileSelectionModel extends AbstractChangeableModel {
   private final Set<File> selectedFiles = new HashSet<>();

   public boolean isEmpty() {
      return this.selectedFiles.isEmpty();
   }

   public boolean isSelected(File file) {
      return this.selectedFiles.contains(file);
   }

   public void setSelected(File file, boolean selected) {
      if (!selected) {
         this.selectedFiles.remove(file);
      } else {
         this.selectedFiles.add(file);
      }

      this.fireChangeEvent();
   }

   public File[] getSelectedFiles() {
      return this.selectedFiles.toArray(new File[0]);
   }

   public void setSelected(File[] files) {
      if (!this.isIdenticalSelection(files)) {
         this.selectedFiles.clear();
         this.selectedFiles.addAll(Arrays.asList(files));
         this.fireChangeEvent();
      }
   }

   private boolean isIdenticalSelection(File[] files) {
      if (this.selectedFiles.size() != files.length) {
         return false;
      } else {
         for (int i = 0; i < files.length; i++) {
            if (!this.isSelected(files[i])) {
               return false;
            }
         }

         return true;
      }
   }
}
