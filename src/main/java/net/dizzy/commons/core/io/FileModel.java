package net.dizzy.commons.core.io;

import java.io.File;

import net.dizzy.commons.core.model.ObjectModel;

public class FileModel extends ObjectModel<File> {
   public FileModel() {
      super();
   }

   public FileModel(File file) {
      super(file);
   }
}
