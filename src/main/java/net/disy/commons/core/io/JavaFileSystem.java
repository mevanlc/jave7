package net.disy.commons.core.io;

import java.io.File;
import java.io.IOException;

public class JavaFileSystem implements IFileSystem {
   @Override
   public IFile createTempFile(String prefix, String postfix, IFile directory) throws IOException {
      File file = File.createTempFile(prefix, postfix, directory.getFile());
      file.deleteOnExit();
      return new JavaFile(file);
   }

   @Override
   public IFile getDefaultTempDir() {
      return new JavaFile(new File(System.getProperty("java.io.tmpdir")));
   }
}
