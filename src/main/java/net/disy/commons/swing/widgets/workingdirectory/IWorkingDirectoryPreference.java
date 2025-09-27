package net.disy.commons.swing.filechooser.workingdirectory;

import java.io.File;
import net.disy.commons.core.io.IWorkingDirectoryProvider;

public interface IWorkingDirectoryPreference extends IWorkingDirectoryProvider {
   void setWorkingDirectory(File var1);
}
