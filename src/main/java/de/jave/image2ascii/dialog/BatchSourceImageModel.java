package de.jave.image2ascii.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.disy.commons.core.model.AbstractChangeableModel;

public class BatchSourceImageModel extends AbstractChangeableModel {
   private final List<File> files;

   public BatchSourceImageModel(File[] files) {
      this.files = new ArrayList<>(Arrays.asList(files));
   }

   public File[] getFiles() {
      return this.files.toArray(new File[0]);
   }

   public void add(File[] filesToAdd) {
      this.files.addAll(Arrays.asList(filesToAdd));
      this.fireChangeEvent();
   }

   public int getFileCount() {
      return this.files.size();
   }

   public File getFile(int index) {
      return this.files.get(index);
   }

   public void removeFileAt(int index) {
      this.files.remove(index);
      this.fireChangeEvent();
   }

   public void moveFileDown(int index) {
      File file = this.files.remove(index);
      this.files.add(index + 1, file);
   }

   public void moveFileUp(int index) {
      File file = this.files.remove(index);
      this.files.add(index - 1, file);
   }
}
