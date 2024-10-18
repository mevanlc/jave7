package de.jave.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.io.IOUtilities;

public class IOTools {
   private IOTools() {
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
         return file.toURI().toURL();
      } catch (MalformedURLException var2) {
         throw new UnreachableCodeReachedException(var2);
      }
   }

   public static boolean isZipped(File file) {
      return isZipped(toURL(file));
   }

   public static boolean isZipped(URL url) {
      DataInputStream inputStream = null;

      boolean var3;
      try {
         inputStream = new DataInputStream(url.openStream());
         return inputStream.readByte() == 80 && inputStream.readByte() == 75 && inputStream.readByte() == 3 && inputStream.readByte() == 4;
      } catch (IOException var7) {
         var3 = false;
      } finally {
         IOUtilities.close(inputStream);
      }

      return var3;
   }

   public static BufferedReader openPossiblyZipped(URL url) throws IOException {
      BufferedReader dis = null;
      if (isZipped(url)) {
         try {
            DataInputStream datis = new DataInputStream(url.openStream());
            ZipInputStream zis = new ZipInputStream(datis);
            zis.getNextEntry();
            dis = new BufferedReader(new InputStreamReader(zis));
         } catch (ZipException var4) {
            System.err.println("ZipException " + var4);
            return null;
         }
      } else {
         dis = new BufferedReader(new InputStreamReader(url.openStream()));
      }

      return dis;
   }

   public static boolean move(String source, String destination) {
      return move(new File(source), new File(destination));
   }

   public static boolean move(File source, File destination) {
      File destDir = new File(new File(destination.getParent()).getAbsolutePath());
      return !destDir.exists() && !destDir.mkdirs() ? false : source.renameTo(destination);
   }

   public static boolean copy(String sourceFileName, String destinationFileName) {
      return copy(new File(sourceFileName), new File(destinationFileName));
   }

   public static boolean copy(File sourceFile, File destinationFile) {
      int bufferSize = 262144;
      BufferedInputStream bis = null;
      BufferedOutputStream bos = null;

      int size;
      int totalSize = 0;
      try {
         byte[] buf = new byte[262144];
         bis = new BufferedInputStream(new FileInputStream(sourceFile));
         bos = new BufferedOutputStream(new FileOutputStream(destinationFile));

         while ((size = bis.read(buf)) > -1) {
            bos.write(buf, 0, size);
            totalSize += size;
         }

         return true;
      } catch (Exception var16) {
         System.out.println("Exception: " + var16.getMessage());
         var16.printStackTrace();
         size = 0;
      } finally {
         try {
            if (bis != null) {
               bis.close();
            }

            if (bos != null) {
               bos.close();
            }
         } catch (IOException var15) {
         }
      }

      return totalSize > 0;
   }
}
