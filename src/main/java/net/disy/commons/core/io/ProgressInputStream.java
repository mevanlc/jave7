package net.disy.commons.core.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.disy.commons.core.util.Ensure;

public class ProgressInputStream extends FilterInputStream {
   private long bytesRead = 0L;
   private final IBytesReadHandler handler;

   public ProgressInputStream(InputStream in, IBytesReadHandler handler) {
      super(in);
      Ensure.ensureArgumentNotNull(handler);
      this.handler = handler;
   }

   @Override
   public int read() throws IOException {
      int result = super.read();
      if (result != -1) {
         this.logBytesRead(1L);
      }

      return result;
   }

   private void logBytesRead(long byteCount) {
      if (byteCount != -1L) {
         this.bytesRead += byteCount;
         this.handler.handleBytesRead(byteCount, this.bytesRead);
      }
   }

   @Override
   public int read(byte[] b) throws IOException {
      int byteCount = super.read(b);
      this.logBytesRead(byteCount);
      return byteCount;
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      int byteCount = super.read(b, off, len);
      this.logBytesRead(byteCount);
      return byteCount;
   }

   @Override
   public long skip(long n) throws IOException {
      long byteCount = super.skip(n);
      this.logBytesRead(byteCount);
      return byteCount;
   }
}
