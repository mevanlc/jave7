package de.jave.lib.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import net.dizzy.commons.core.io.IOUtilities;

public class IoUtilities {
   private IoUtilities() {
   }

   public static URL toUrl(String name) {
      URL url = null;

      try {
         url = new URL(name);
      } catch (MalformedURLException var3) {
         url = toURL(new File(name));
      }

      return url;
   }

   public static String toURLString(File file) {
      return toURL(file).toExternalForm();
   }

   public static URL toURL(File file) {
      try {
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
      } catch (MalformedURLException var2) {
         return null;
      }
   }

   public static boolean move(String source, String destination) {
      return move(new File(source), new File(destination));
   }

   public static boolean move(File source, File destination) {
      File destDir = new File(new File(destination.getParent()).getAbsolutePath());
      return !destDir.exists() && !destDir.mkdirs() ? false : source.renameTo(destination);
   }

   public static void copy(String sourceFileName, String destinationFileName) throws IOException {
      copy(new File(sourceFileName), new File(destinationFileName));
   }

   public static void copy(File sourceFile, File destinationFile) throws IOException {
      if (!ensureFoldersExist(destinationFile.getParentFile())) {
         throw new IOException("Unable to create necessary output directory " + destinationFile.getParentFile());
      } else {
         BufferedInputStream bis = null;
         BufferedOutputStream bos = null;

         try {
            bis = new BufferedInputStream(new FileInputStream(sourceFile));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFile));
            IOUtilities.copyStream(bis, bos);
         } finally {
            IOUtilities.close(bis);
            IOUtilities.close(bos);
         }
      }
   }

   public static boolean ensureFoldersExist(File folder) {
      return folder.exists() ? true : folder.mkdirs();
   }

   public static final String getDisplayFilename(String original, int maxLength) {
      int length = original.length();
      if (length <= maxLength) {
         return original;
      } else {
         int i2 = original.lastIndexOf(File.separatorChar);
         return length - i2 + 3 + 3 <= maxLength
            ? original.substring(0, maxLength - 4 - (length - i2)) + "..." + original.substring(i2)
            : original.substring(0, 3) + "..." + original.substring(length - 1 - maxLength + 6);
      }
   }

   public static final String getDisplayFilename(File file, int maxLength) {
      return getDisplayFilename(file.getAbsolutePath(), maxLength);
   }

   public static String readString(Reader reader) throws IOException {
      StringBuffer buffer = new StringBuffer();
      char[] buf = new char[4096];
      int numChars = 0;

      while ((numChars = reader.read(buf)) > 0) {
         buffer.append(buf, 0, numChars);
      }

      return buffer.toString();
   }

   public static byte[] readBytes(InputStream inputStream) throws IOException {
      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      IOUtilities.copyStream(inputStream, byteOut);
      return byteOut.toByteArray();
   }

   public static File exchangeFileExtension(File file, String newFileExtension) {
      String fileName = file.getAbsolutePath();
      int index = fileName.lastIndexOf(46);
      if (index == -1) {
         throw new IllegalStateException("Unable to determine file extension from file name '" + fileName + "'");
      } else {
         return new File(fileName.substring(0, index + 1) + newFileExtension);
      }
   }
}
