package net.jmge.gif.facade;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.dizzy.commons.core.io.IOUtilities;
import net.jmge.gif.Gif89Frame;
import net.jmge.gif.GifColorTable;
import net.jmge.gif.TooManyColorsException;
import net.jmge.gif.encode.Gif89Encoder;

public class AnimatedGifFileBuilder {
   private final Dimension dispDim = new Dimension(0, 0);
   private final GifColorTable colorTable = new GifColorTable();
   private final TmpFileFrameContainer frameContainer = new TmpFileFrameContainer();
   private String comment;
   private int loopCount = 1;

   public void setComments(String comment) {
      this.comment = comment;
   }

   public void setLoopCount(int loopCount) {
      this.loopCount = loopCount;
   }

   public void add(BufferedImage image, int frameMillis) throws TooManyColorsException, IOException {
      Gif89Frame gifFrame = new Gif89Frame(image);
      gifFrame.setDelay(frameMillis / 10);
      this.dispDim.width = Math.max(this.dispDim.width, image.getWidth());
      this.dispDim.height = Math.max(this.dispDim.height, image.getHeight());
      this.colorTable.processPixels(gifFrame);
      this.frameContainer.add(gifFrame);
   }

   public void write(File file) throws IOException {
      this.colorTable.closePixelProcessing();
      Gif89Encoder gifEncoder = new Gif89Encoder();
      gifEncoder.setComments(this.comment);
      gifEncoder.setLoopCount(this.loopCount);
      FileOutputStream out = null;

      try {
         out = new FileOutputStream(file);
         OutputStream output = new BufferedOutputStream(out);
         gifEncoder.encode(output, this.frameContainer, this.colorTable, this.dispDim);
         output.flush();
      } finally {
         this.frameContainer.cleanUp();
         IOUtilities.close(out);
      }
   }
}
