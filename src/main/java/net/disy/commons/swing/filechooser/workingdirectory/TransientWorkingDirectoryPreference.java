package net.disy.commons.swing.filechooser.workingdirectory;

import java.io.File;

public class TransientWorkingDirectoryPreference implements IWorkingDirectoryPreference {
   private File workingDirectory;

   public TransientWorkingDirectoryPreference() {
      this(null);
   }

   public TransientWorkingDirectoryPreference(File workingDirectory) {
      this.workingDirectory = workingDirectory;
   }

   @Override
   public File getWorkingDirectory() {
      return this.workingDirectory;
   }

   @Override
   public void setWorkingDirectory(File workingDirectory) {
      this.workingDirectory = workingDirectory;
   }
}
