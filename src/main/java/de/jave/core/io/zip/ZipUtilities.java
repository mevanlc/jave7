package de.jave.core.io.zip;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

public class ZipUtilities {
   private static InputStream openZippedStream(InputStream inputStream) throws IOException {
      ZipInputStream zis = new ZipInputStream(inputStream);
      zis.getNextEntry();
      return zis;
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
}
