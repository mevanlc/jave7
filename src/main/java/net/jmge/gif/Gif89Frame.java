package net.jmge.gif;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public final class Gif89Frame implements Serializable {
   private final Dimension size;
   private final byte[] ciPixels;
   private Point position = new Point(0, 0);
   private boolean isInterlaced;
   private int csecsDelay;
   private GifFrameDisposeMode disposeMode = GifFrameDisposeMode.LEAVE;
   private final int[] argbPixels;

   public Gif89Frame(BufferedImage image) {
      this.size = new Dimension(image.getWidth(), image.getHeight());
      this.argbPixels = new int[this.size.width * this.size.height];

      for (int y = 0; y < this.size.height; y++) {
         for (int x = 0; x < this.size.width; x++) {
            this.argbPixels[y * this.size.width + x] = image.getRGB(x, y);
         }
      }

      this.ciPixels = new byte[this.argbPixels.length];
   }

   public int[] getPixelSource() {
      return this.argbPixels;
   }

   private void setPosition(Point p) {
      this.position = new Point(p);
   }

   private void setInterlaced(boolean b) {
      this.isInterlaced = b;
   }

   public void setDelay(int interval) {
      this.csecsDelay = interval;
   }

   private void setDisposalMode(GifFrameDisposeMode disposeMode) {
      this.disposeMode = disposeMode;
   }

   public byte[] getPixelSink() {
      return this.ciPixels;
   }

   public boolean isInterlaced() {
      return this.isInterlaced;
   }

   public Dimension getSize() {
      return this.size;
   }

   public Point getPosition() {
      return this.position;
   }

   public GifFrameDisposeMode getDisposeMode() {
      return this.disposeMode;
   }

   public int getCsecsDelay() {
      return this.csecsDelay;
   }
}
