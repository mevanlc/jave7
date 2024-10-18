package net.disy.commons.core.testing.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.disy.commons.core.io.AbstractFile;
import net.disy.commons.core.io.IFile;
import net.disy.commons.core.io.IOUtilities;
import net.disy.commons.core.predicate.IPredicate;
import net.disy.commons.core.util.CollectionUtilities;

public class DummyFile extends AbstractFile {
   private boolean exists;
   private byte[] content;
   private final Map<String, IFile> children = new HashMap<>();
   private String name;

   public static DummyFile CreateExisting(String name, byte... content) throws Exception {
      DummyFile dummyFile = new DummyFile(name);
      dummyFile.create(content);
      return dummyFile;
   }

   public static DummyFile CreateDirectory(String dirName) {
      DummyFile dummyFile = new DummyFile(dirName);
      dummyFile.exists = true;
      return dummyFile;
   }

   public DummyFile(String name) {
      this.name = name;
   }

   @Override
   public boolean mkDirs() {
      throw new UnsupportedOperationException("Dummy");
   }

   @Deprecated
   @Override
   public IFile[] listFiles(FileFilter fileFilter) {
      throw new UnsupportedOperationException("Dummy");
   }

   @Override
   public boolean isDirectory() {
      return this.exists && this.content == null;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Deprecated
   @Override
   public File getFile() {
      throw new UnsupportedOperationException("Dummy");
   }

   @Override
   public IFile getChild(String child) {
      if (!this.children.containsKey(child)) {
         this.children.put(child, new DummyFile(child));
      }

      return this.children.get(child);
   }

   @Override
   public String getAbsolutePath() {
      return "/d/ummy/" + this.name;
   }

   @Override
   public boolean exists() {
      return this.exists;
   }

   @Override
   public Reader createReader() throws FileNotFoundException {
      if (this.content == null) {
         throw new FileNotFoundException(this.name);
      } else {
         return new StringReader(new String(this.content));
      }
   }

   @Override
   public OutputStream createOutputStream() {
      return new ByteArrayOutputStream() {
         @Override
         public void close() throws IOException {
            super.close();
            DummyFile.this.create(this.toByteArray());
         }
      };
   }

   @Override
   public void createNew() throws IOException {
      if (this.exists()) {
         throw new IOException("File already exists");
      } else {
         byte[] newContent = new byte[0];
         this.create(newContent);
      }
   }

   private void create(byte[] newContent) {
      this.content = newContent;
      this.exists = true;
   }

   @Override
   public InputStream openInputStream() {
      if (this.content == null) {
         throw new IllegalStateException("File not found " + this.name);
      } else {
         return new ByteArrayInputStream(this.content);
      }
   }

   @Override
   public IFile[] getChildren(IPredicate<IFile> predicate) {
      List<IFile> files = CollectionUtilities.filter(this.children.values(), predicate);
      return files.toArray(new IFile[0]);
   }

   public DummyFile createSubdirectory(String dirName) {
      DummyFile directoryFile = CreateDirectory(dirName);
      this.addChild(directoryFile);
      return directoryFile;
   }

   public DummyFile createExistingChild(String child, byte... bytes) throws Exception {
      DummyFile newFile = CreateExisting(child, bytes);
      this.addChild(newFile);
      return newFile;
   }

   public void addChild(DummyFile newFile) {
      this.children.put(newFile.name, newFile);
   }

   public DummyFile createNonExistingChild(String child) {
      DummyFile file = new DummyFile(child);
      this.addChild(file);
      return file;
   }

   @Override
   public void delete() {
      this.exists = false;
      this.content = null;
   }

   @Override
   public void rename(String newName) {
      this.name = newName;
   }

   @Override
   public void setContent(InputStream content) throws IOException {
      InputStreamReader contentReader = new InputStreamReader(content);
      this.create(IOUtilities.readString(contentReader).getBytes());
   }
}
