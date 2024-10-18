package net.disy.commons.core.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import net.disy.commons.core.predicate.IPredicate;
import net.disy.commons.core.util.ArrayUtilities;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.ITransformer;

public class JavaFile extends AbstractFile {
   private final File file;

   public JavaFile(File file) {
      Ensure.ensureNotNull("Cannot wrap around null.", file);
      this.file = file;
   }

   @Deprecated
   @Override
   public File getFile() {
      return this.file;
   }

   @Override
   public String toString() {
      return this.file.getAbsolutePath();
   }

   @Override
   public boolean mkDirs() {
      return this.file.mkdirs();
   }

   @Override
   public boolean equals(Object obj) {
      return !(obj instanceof JavaFile) ? false : ((JavaFile)obj).file.equals(this.file);
   }

   @Override
   public int hashCode() {
      return this.file.hashCode() * 3;
   }

   @Override
   public boolean exists() {
      return this.file.exists();
   }

   @Override
   public IFile getChild(String child) {
      File childFile = new File(this.getFile(), child);
      return new JavaFile(childFile);
   }

   @Override
   public IFile[] getChildren(IPredicate<IFile> predicate) {
      return this.listFiles(new JavaFile.PredicateFileFilter(predicate));
   }

   @Override
   public String getAbsolutePath() {
      return this.file.getAbsolutePath();
   }

   @Override
   public String getName() {
      return this.file.getName();
   }

   @Deprecated
   @Override
   public IFile[] listFiles(FileFilter fileFilter) {
      File[] files = this.file.listFiles(fileFilter);
      return files == null ? new IFile[0] : ArrayUtilities.transform(files, IFile.class, new ITransformer<File, IFile>() {
         public IFile transform(File input) {
            return new JavaFile(input);
         }
      });
   }

   @Override
   public boolean isDirectory() {
      return this.file.isDirectory();
   }

   @Override
   public OutputStream createOutputStream() throws FileNotFoundException {
      return new FileOutputStream(this.file);
   }

   @Override
   public Reader createReader() throws FileNotFoundException {
      return new FileReader(this.file);
   }

   @Override
   public void createNew() throws IOException {
      if (!this.file.getParentFile().exists()) {
         this.file.getParentFile().mkdirs();
      }

      this.file.createNewFile();
   }

   @Override
   public InputStream openInputStream() throws FileNotFoundException {
      return new FileInputStream(this.file);
   }

   @Override
   public void delete() {
      this.file.delete();
   }

   @Override
   public void rename(String newName) {
      this.file.renameTo(new File(this.file.getParent(), newName));
   }

   @Override
   public void setContent(InputStream content) throws IOException {
      OutputStream outputStream = null;

      try {
         outputStream = this.createOutputStream();
         IOUtilities.copyStream(content, outputStream);
      } finally {
         IOUtilities.close(outputStream);
      }
   }

   public static final class PredicateFileFilter implements FileFilter {
      private final IPredicate<IFile> predicate;

      public PredicateFileFilter(IPredicate<IFile> predicate) {
         this.predicate = predicate;
      }

      @Override
      public boolean accept(File pathname) {
         return this.predicate.evaluate(new JavaFile(pathname));
      }
   }
}
