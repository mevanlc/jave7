package net.disy.commons.core.io;

public abstract class AbstractFile implements IFile {
   @Override
   public boolean hasExtension(String extension) {
      if (!extension.startsWith(".")) {
         extension = "." + extension;
      }

      return this.getName().endsWith(extension);
   }
}
