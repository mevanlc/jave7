package net.disy.commons.core.io;

import java.io.IOException;

public interface IFileSystem {
   IFile createTempFile(String var1, String var2, IFile var3) throws IOException;

   IFile getDefaultTempDir();
}
