package net.jmge.gif.encode;

import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import net.jmge.gif.Gif89Frame;
import net.jmge.gif.GifColorTable;
import net.jmge.gif.IGifFrameProvider;

public class Gif89Encoder {
   private final int bgIndex = 0;
   private int loopCount = 1;
   private String theComments;

   public void setLoopCount(int count) {
      this.loopCount = count;
   }

   public void setComments(String comments) {
      this.theComments = comments;
   }

   public void encode(OutputStream out, IGifFrameProvider frameContainter, GifColorTable colorTable, Dimension dispDim) throws IOException {
      int nframes = frameContainter.getSize();
      boolean is_sequence = nframes > 1;
      Put.ascii("GIF89a", out);
      this.writeLogicalScreenDescriptor(out, colorTable, dispDim);
      ColorTableEncoder colorTableEncoder = new ColorTableEncoder(colorTable);
      colorTableEncoder.encode(out);
      if (is_sequence && this.loopCount != 1) {
         this.writeNetscapeExtension(out);
      }

      if (this.theComments != null && this.theComments.length() > 0) {
         this.writeCommentExtension(out);
      }

      for (int i = 0; i < nframes; i++) {
         Gif89Frame frame = frameContainter.get(i);
         GifFrameEncoder frameEncoder = new GifFrameEncoder(frame);
         frameEncoder.encode(out, is_sequence, colorTable.getDepth(), colorTable.getTransparent());
      }

      out.write(59);
      out.flush();
   }

   private void writeLogicalScreenDescriptor(OutputStream os, GifColorTable colorTable, Dimension dispDim) throws IOException {
      Put.leShort(dispDim.width, os);
      Put.leShort(dispDim.height, os);
      os.write(240 | colorTable.getDepth() - 1);
      os.write(0);
      os.write(0);
   }

   private void writeNetscapeExtension(OutputStream os) throws IOException {
      os.write(33);
      os.write(255);
      os.write(11);
      Put.ascii("NETSCAPE2.0", os);
      os.write(3);
      os.write(1);
      Put.leShort(this.loopCount > 1 ? this.loopCount - 1 : 0, os);
      os.write(0);
   }

   private void writeCommentExtension(OutputStream os) throws IOException {
      os.write(33);
      os.write(254);
      int remainder = this.theComments.length() % 255;
      int nsubblocks_full = this.theComments.length() / 255;
      int nsubblocks = nsubblocks_full + (remainder > 0 ? 1 : 0);
      int ibyte = 0;

      for (int isb = 0; isb < nsubblocks; isb++) {
         int size = isb < nsubblocks_full ? 255 : remainder;
         os.write(size);
         Put.ascii(this.theComments.substring(ibyte, ibyte + size), os);
         ibyte += size;
      }

      os.write(0);
   }
}
