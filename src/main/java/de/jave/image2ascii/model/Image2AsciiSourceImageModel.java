package de.jave.image2ascii.model;

import java.io.File;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.util.Ensure;

public class Image2AsciiSourceImageModel extends AbstractChangeableModel {
   private SourceImageContainer sourceImage;
   private final FileModel currentDirectoryModel;
   private final FileModel fileModel = new FileModel();

   public Image2AsciiSourceImageModel(FileModel currentDirectoryModel) {
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      this.currentDirectoryModel = currentDirectoryModel;
   }

   public void set(File file, SourceImageContainer sourceImage) {
      this.fileModel.setValue(file);
      this.sourceImage = sourceImage;
      this.fireChangeEvent();
   }

   public boolean isEmpty() {
      return this.sourceImage == null;
   }

   public SourceImageContainer getSourceImage() {
      return this.sourceImage;
   }

   public FileModel getFileModel() {
      return this.fileModel;
   }

   public void setCurrentDirectory(File currentDirectory) {
      this.currentDirectoryModel.setValue(currentDirectory);
   }

   public FileModel getCurrentDirectoryModel() {
      return this.currentDirectoryModel;
   }
}
