package net.disy.commons.swing.filechooser.model;

import javax.swing.filechooser.FileFilter;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.util.ObjectUtilities;

public class FileFilterModel extends AbstractChangeableModel {
   private FileFilter fileFilter;

   public FileFilter getFileFilter() {
      return this.fileFilter;
   }

   public void setFileFilter(FileFilter fileFilter) {
      if (!ObjectUtilities.equals(fileFilter, this.fileFilter)) {
         this.fileFilter = fileFilter;
         this.fireChangeEvent();
      }
   }
}
