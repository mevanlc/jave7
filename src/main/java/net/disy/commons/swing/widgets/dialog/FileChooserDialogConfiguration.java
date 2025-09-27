package net.disy.commons.swing.filechooser.dialog;

import java.io.File;
import javax.swing.filechooser.FileSystemView;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.util.Ensure;

public class FileChooserDialogConfiguration implements Cloneable {
   private final FileSystemView fileSystemView;
   private final File[] roots;
   private final IFileModelActionFactory[] actionFactories;
   private boolean fileFilterEnabled = true;

   public FileChooserDialogConfiguration() {
      this(FileSystemView.getFileSystemView(), FileSystemView.getFileSystemView().getRoots());
   }

   public FileChooserDialogConfiguration(File[] roots) {
      this(FileSystemView.getFileSystemView(), roots);
   }

   public FileChooserDialogConfiguration(FileSystemView fileSystemView, File[] roots) {
      this(fileSystemView, roots, new IFileModelActionFactory[0]);
   }

   public FileChooserDialogConfiguration(File[] roots, IFileModelActionFactory[] actionFactories) {
      this(FileSystemView.getFileSystemView(), roots, actionFactories);
   }

   public FileChooserDialogConfiguration(FileSystemView fileSystemView, File[] roots, IFileModelActionFactory[] actionFactories) {
      Ensure.ensureArgumentNotNull(fileSystemView);
      Ensure.ensureArgumentNotNull(roots);
      Ensure.ensureArgumentNotNull(actionFactories);
      this.fileSystemView = fileSystemView;
      this.roots = roots;
      this.actionFactories = actionFactories;
   }

   public FileSystemView getFileSystemView() {
      return this.fileSystemView;
   }

   public File[] getRoots() {
      return this.roots;
   }

   public IFileModelActionFactory[] getActionFactories() {
      return this.actionFactories;
   }

   public boolean isFileFilterEnabled() {
      return this.fileFilterEnabled;
   }

   public void setFileFilterEnabled(boolean fileFilterEnabled) {
      this.fileFilterEnabled = fileFilterEnabled;
   }

   public FileChooserDialogConfiguration getClone() {
      try {
         return (FileChooserDialogConfiguration)this.clone();
      } catch (CloneNotSupportedException var2) {
         throw new UnreachableCodeReachedException();
      }
   }
}
