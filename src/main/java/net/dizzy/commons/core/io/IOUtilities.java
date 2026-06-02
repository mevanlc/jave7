package net.dizzy.commons.core.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public final class IOUtilities {
   private static final int BUFFER_SIZE = 8192;

   private IOUtilities() {
   }

   public static void close(Closeable closeable) {
      if (closeable != null) {
         try {
            closeable.close();
         } catch (IOException ignored) {
         }
      }
   }

   public static void copyStream(Reader reader, Writer writer) throws IOException {
      char[] buffer = new char[BUFFER_SIZE];
      int read;
      while ((read = reader.read(buffer)) != -1) {
         writer.write(buffer, 0, read);
      }
   }

   public static void copyStream(InputStream input, OutputStream output) throws IOException {
      byte[] buffer = new byte[BUFFER_SIZE];
      int read;
      while ((read = input.read(buffer)) != -1) {
         output.write(buffer, 0, read);
      }
   }

   public static void copy(File source, File target) throws IOException {
      FileInputStream input = null;
      FileOutputStream output = null;
      try {
         input = new FileInputStream(source);
         output = new FileOutputStream(target);
         copyStream(input, output);
      } finally {
         close(input);
         close(output);
      }
   }

   public static String readString(Reader reader) throws IOException {
      StringBuilder builder = new StringBuilder();
      char[] buffer = new char[BUFFER_SIZE];
      int read;
      while ((read = reader.read(buffer)) != -1) {
         builder.append(buffer, 0, read);
      }
      return builder.toString();
   }
}
