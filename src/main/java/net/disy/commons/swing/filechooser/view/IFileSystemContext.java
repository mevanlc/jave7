package net.disy.commons.swing.filechooser.view;

import java.io.File;
import javax.swing.filechooser.FileSystemView;

public interface IFileSystemContext {
   FileSystemView getFileSystemView();

   void setBusy(boolean var1);

   void registerNode(File var1, FolderNode var2);
}
