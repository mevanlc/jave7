package net.jmge.gif.facade;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.dizzy.commons.core.io.IOUtilities;
import net.dizzy.commons.core.util.Ensure;
import net.jmge.gif.Gif89Frame;
import net.jmge.gif.GifColorTable;
import net.jmge.gif.IGifFrameProvider;
import net.jmge.gif.SingleFrameProvider;
import net.jmge.gif.TooManyColorsException;
import net.jmge.gif.encode.Gif89Encoder;

public class GifFileWriter {
   private final BufferedImage image;
   private String comments;

   public GifFileWriter(BufferedImage image) {
      Ensure.ensureArgumentNotNull(image);
      this.image = image;
   }

   public void setComments(String comments) {
      this.comments = comments;
   }

   public void write(File file) throws IOException, TooManyColorsException {
      OutputStream output = new BufferedOutputStream(new FileOutputStream(file));
      Gif89Frame frame = new Gif89Frame(this.image);
      GifColorTable colorTable = new GifColorTable();
      colorTable.processPixels(frame);
      colorTable.closePixelProcessing();
      Gif89Encoder encoder = new Gif89Encoder();
      encoder.setComments(this.comments);
      IGifFrameProvider frameProvider = new SingleFrameProvider(frame);
      encoder.encode(output, frameProvider, colorTable, new Dimension(this.image.getWidth(), this.image.getHeight()));
      IOUtilities.close(output);
   }
}
