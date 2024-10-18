package de.jdemo.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Markus Gebhard
 */
public class IOUtilities {
  private IOUtilities() {
    //not instantiable
  }

  public static void close(final Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      }
      catch (final IOException e) {
        //ignore
      }
    }
  }

  public static void copy(final File sourceFile, final File destinationFile) throws IOException {
    if (!ensureFoldersExist(destinationFile.getParentFile())) {
      throw new IOException("Unable to create necessary output directory " + destinationFile.getParentFile());
    }
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    try {
      bis = new BufferedInputStream(new FileInputStream(sourceFile));
      bos = new BufferedOutputStream(new FileOutputStream(destinationFile));
      copyStream(bis, bos);
    }
    finally {
      close(bis);
      close(bos);
    }
  }

  public static boolean ensureFoldersExist(final File folder) {
    if (folder.exists()) {
      return true;
    }
    return folder.mkdirs();
  }

  public static void copyStream(final InputStream in, final OutputStream out) throws IOException {
    //TODO 12.12.2003 (gebhard): BufferedStreams zur Performanceverbesserung?
    final byte[] buffer = new byte[4096];
    while (true) {
      final int bytesRead = in.read(buffer);
      if (bytesRead == -1) {
        break;
      }
      out.write(buffer, 0, bytesRead);
    }
  }
}