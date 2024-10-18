package net.disy.commons.core.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.ProgressUtilities;
import net.disy.commons.core.util.Ensure;

public class CancelableInputStream extends FilterInputStream {
   private final ICancelable cancelable;

   public CancelableInputStream(ICancelable cancelable, InputStream inputStream) {
      super(inputStream);
      Ensure.ensureArgumentNotNull(cancelable);
      this.cancelable = cancelable;
   }

   @Override
   public int read() throws IOException {
      ProgressUtilities.checkInterruptedIO(this.cancelable);
      return super.read();
   }

   @Override
   public int available() throws IOException {
      ProgressUtilities.checkInterruptedIO(this.cancelable);
      return super.available();
   }

   @Override
   public int read(byte[] b) throws IOException {
      ProgressUtilities.checkInterruptedIO(this.cancelable);
      return super.read(b);
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      ProgressUtilities.checkInterruptedIO(this.cancelable);
      return super.read(b, off, len);
   }

   @Override
   public long skip(long n) throws IOException {
      ProgressUtilities.checkInterruptedIO(this.cancelable);
      return super.skip(n);
   }
}
