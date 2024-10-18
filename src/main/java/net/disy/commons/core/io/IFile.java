package net.disy.commons.core.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import net.disy.commons.core.predicate.IPredicate;

public interface IFile {
   boolean exists();

   String getAbsolutePath();

   IFile getChild(String var1);

   String getName();

   boolean isDirectory();

   @Deprecated
   IFile[] listFiles(FileFilter var1);

   IFile[] getChildren(IPredicate<IFile> var1);

   boolean mkDirs();

   void createNew() throws IOException;

   Reader createReader() throws FileNotFoundException;

   OutputStream createOutputStream() throws FileNotFoundException;

   @Deprecated
   File getFile();

   InputStream openInputStream() throws FileNotFoundException;

   boolean hasExtension(String var1);

   void delete();

   void rename(String var1);

   void setContent(InputStream var1) throws IOException;
}
