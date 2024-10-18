package de.jave.javeplayer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

public class ZIPTool {
   private ZIPTool() {
   }

   public static URL toUrl(String name) {
      URL url = null;

      try {
         url = new URL(name);
      } catch (MalformedURLException var6) {
         File file = new File(name);

         try {
            url = toURL(file);
         } catch (MalformedURLException var5) {
            return null;
         }
      }

      return url;
   }

   public static URL toURL(File file) throws MalformedURLException {
      String path = file.getAbsolutePath();
      if (File.separatorChar != '/') {
         path = path.replace(File.separatorChar, '/');
      }

      if (!path.startsWith("/")) {
         path = "/" + path;
      }

      if (!path.endsWith("/") && file.isDirectory()) {
         path = path + "/";
      }

      return new URL("file", "", path);
   }

   public static InputStream openPossiblyZipped(InputStream inputStream) throws IOException {
      DataInputStream in = new DataInputStream(new BufferedInputStream(inputStream));
      in.mark(10);
      boolean isZipped = false;

      try {
         isZipped = in.readByte() == 80 && in.readByte() == 75 && in.readByte() == 3 && in.readByte() == 4;
      } catch (EOFException var4) {
         isZipped = false;
      }

      in.reset();
      return isZipped ? openZippedStream(in) : in;
   }

   private static InputStream openZippedStream(InputStream inputStream) throws IOException {
      try {
         ZipInputStream zis = new ZipInputStream(inputStream);
         zis.getNextEntry();
         return zis;
      } catch (ZipException var2) {
         throw new IOException("ZipException " + var2);
      }
   }
}
