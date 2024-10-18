package de.jave.image.monochrome;

import de.jave.image.GImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class GMonochromeImage extends GImage {
   private int[][] pixels;

   public GMonochromeImage(Dimension size) {
      this(size.width, size.height);
   }

   public GMonochromeImage(int width, int height) {
      super(width, height);
      this.pixels = new int[height][(width + 31) / 32];
   }

   public GMonochromeImage(int[][] pixels) {
      super(pixels.length, pixels[0].length);
      this.pixels = new int[this.getHeight()][(this.getWidth() + 31) / 32];

      for (int y = 0; y < this.getHeight(); y++) {
         for (int x = 0; x < this.getWidth(); x++) {
            this.set(x, y, pixels[x][y]);
         }
      }
   }

   public GMonochromeImage(int[] p, int width, int height) {
      super(width, height);
      this.pixels = new int[height][(width + 31) / 32];

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            int originalPixel = p[y * width + x];
            int value = (299 * (originalPixel >> 16 & 0xFF) + 587 * (originalPixel >> 8 & 0xFF) + 114 * (originalPixel & 0xFF)) / 1000 / 128;
            this.set(x, y, value);
         }
      }
   }

   @Override
   protected GImage createGImage(int width, int height) {
      return new GMonochromeImage(width, height);
   }

   @Override
   public GImage getClone() {
      int w = (this.getWidth() + 31) / 32;
      int[][] p = new int[this.getHeight()][w];

      for (int y = 0; y < this.getHeight(); y++) {
         System.arraycopy(this.pixels[y], 0, p[y], 0, w);
      }

      GMonochromeImage g = new GMonochromeImage(this.getWidth(), this.getHeight());
      g.pixels = p;
      return g;
   }

   public GMonochromeImage scaleY2() {
      GMonochromeImage g = new GMonochromeImage(this.getWidth(), this.getHeight() * 2);
      int w = (this.getWidth() + 31) / 32;
      g.pixels = new int[g.getHeight()][w];

      for (int y = 0; y < this.getHeight(); y++) {
         System.arraycopy(this.pixels[y], 0, g.pixels[y * 2], 0, w);
         System.arraycopy(this.pixels[y], 0, g.pixels[y * 2 + 1], 0, w);
      }

      return g;
   }

   @Override
   public final void set(int x, int y, int value) {
      this.pixels[y][x / 32] = this.pixels[y][x / 32] & -1 - (1 << x % 32);
      this.pixels[y][x / 32] = this.pixels[y][x / 32] | value << x % 32;
   }

   @Override
   public final int get(int x, int y) {
      return this.pixels[y][x / 32] >> x % 32 & 1;
   }

   @Override
   public int getNormalizingFactor() {
      return 255;
   }

   @Override
   public int getMaxPossibleValue() {
      return 1;
   }

   public int countPixels() {
      int result = 0;

      for (int y = 0; y < this.getHeight(); y++) {
         for (int x = 0; x < this.getWidth(); x++) {
            result += 1 - this.get(x, y);
         }
      }

      return result;
   }

   public void edgeDespecle() {
      int height = this.getHeight();
      int width = this.getWidth();

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            if (this.get(x, y) == 0) {
               int count = this.getNeighbourCount(x, y);
               if (count == 0) {
                  this.set(x, y, 1);
               } else if (count == 1) {
                  if (x < width - 1 && this.get(x + 1, y) == 0 && this.getNeighbourCount(x + 1, y) == 1) {
                     this.set(x, y, 1);
                     this.set(x + 1, y, 1);
                  } else if (y > 0 && x < width - 1 && this.get(x + 1, y - 1) == 0 && this.getNeighbourCount(x + 1, y - 1) == 1) {
                     this.set(x, y, 1);
                     this.set(x + 1, y - 1, 1);
                  } else if (y < height - 1 && x < width - 1 && this.get(x + 1, y + 1) == 0 && this.getNeighbourCount(x + 1, y + 1) == 1) {
                     this.set(x, y, 1);
                     this.set(x + 1, y + 1, 1);
                  } else if (y < height - 1 && this.get(x, y + 1) == 0 && this.getNeighbourCount(x, y + 1) == 1) {
                     this.set(x, y, 1);
                     this.set(x, y + 1, 1);
                  }
               }
            }
         }
      }
   }

   public int getNeighbourCount(int x, int y) {
      int width = this.getWidth();
      int height = this.getHeight();
      int result = 0;
      if (x > 0 && this.get(x - 1, y) == 0) {
         result++;
      }

      if (x < width - 1 && this.get(x + 1, y) == 0) {
         result++;
      }

      if (y > 0 && this.get(x, y - 1) == 0) {
         result++;
      }

      if (y < height - 1 && this.get(x, y + 1) == 0) {
         result++;
      }

      if (y > 0 && x > 0 && this.get(x - 1, y - 1) == 0) {
         result++;
      }

      if (y > 0 && x < width - 1 && this.get(x + 1, y - 1) == 0) {
         result++;
      }

      if (y < height - 1 && x > 0 && this.get(x - 1, y + 1) == 0) {
         result++;
      }

      if (y < height - 1 && x < width - 1 && this.get(x + 1, y + 1) == 0) {
         result++;
      }

      return result;
   }

   @Override
   public void paint(Graphics g, int x0, int y0) {
      g.setColor(Color.white);
      g.fillRect(x0, x0, this.getWidth(), this.getHeight());
      g.setColor(Color.black);

      for (int y = this.getHeight() - 1; y >= 0; y--) {
         for (int x = this.getWidth() - 1; x >= 0; x--) {
            if (this.get(x, y) == 0) {
               g.drawLine(x0 + x, y0 + y, x0 + x, y0 + y);
            }
         }
      }
   }
}
