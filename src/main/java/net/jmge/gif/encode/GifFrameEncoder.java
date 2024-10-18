package net.jmge.gif.encode;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.io.OutputStream;
import net.disy.commons.core.util.Ensure;
import net.jmge.gif.Gif89Frame;
import net.jmge.gif.GifFrameDisposeMode;

public class GifFrameEncoder {
   private final Gif89Frame frame;

   public GifFrameEncoder(Gif89Frame frame) {
      Ensure.ensureArgumentNotNull(frame);
      this.frame = frame;
   }

   public void encode(OutputStream os, boolean epluribus, int color_depth, int transparent_index) throws IOException {
      this.writeGraphicControlExtension(os, epluribus, transparent_index, this.frame.getDisposeMode(), this.frame.getCsecsDelay());
      this.writeImageDescriptor(os, this.frame.getSize(), this.frame.getPosition(), this.frame.isInterlaced());
      GifPixelsEncoder pixelsEncoder = new GifPixelsEncoder(this.frame.getSize(), this.frame.getPixelSink(), this.frame.isInterlaced(), color_depth);
      pixelsEncoder.encode(os);
   }

   private void writeGraphicControlExtension(OutputStream os, boolean epluribus, int itransparent, GifFrameDisposeMode gifFrameDisposeMode, int csecsDelay) throws IOException {
      int transflag = itransparent == -1 ? 0 : 1;
      if (transflag == 1 || epluribus) {
         os.write(33);
         os.write(249);
         os.write(4);
         os.write(gifFrameDisposeMode.getValue() << 2 | transflag);
         Put.leShort(csecsDelay, os);
         os.write(itransparent);
         os.write(0);
      }
   }

   private void writeImageDescriptor(OutputStream os, Dimension size, Point position, boolean interlaced) throws IOException {
      os.write(44);
      Put.leShort(position.x, os);
      Put.leShort(position.y, os);
      Put.leShort(size.width, os);
      Put.leShort(size.height, os);
      os.write(interlaced ? 64 : 0);
   }
}
