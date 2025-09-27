package net.disy.commons.swing.filechooser.model;

import javax.swing.filechooser.FileFilter;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.model.listener.IChangeListener;

public class FileChooserModel extends AbstractChangeableModel {
   private final FileModel folderModel;
   private final FileModel fileModel;
   private final FileFilterModel fileFilterModel = new FileFilterModel();
   private final ChoosableFileFilterModel choosableFileFilterModel = new ChoosableFileFilterModel();

   public FileChooserModel() {
      this(new FileModel(), new FileModel());
   }

   public FileChooserModel(FileModel folderModel, FileModel fileModel) {
      this.folderModel = folderModel;
      this.fileModel = fileModel;
      this.choosableFileFilterModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            if (FileChooserModel.this.choosableFileFilterModel.isEmpty()) {
               FileChooserModel.this.fileFilterModel.setFileFilter(null);
            } else {
               FileFilter[] fileFilters = FileChooserModel.this.choosableFileFilterModel.getFileFilters();
               FileChooserModel.this.fileFilterModel.setFileFilter(fileFilters[fileFilters.length - 1]);
            }
         }
      });
   }

   public FileModel getFileModel() {
      return this.fileModel;
   }

   public FileModel getFolderModel() {
      return this.folderModel;
   }

   public FileFilterModel getFileFilterModel() {
      return this.fileFilterModel;
   }

   public ChoosableFileFilterModel getChoosableFileFilterModel() {
      return this.choosableFileFilterModel;
   }
}
