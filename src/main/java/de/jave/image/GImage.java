package de.jave.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.MemoryImageSource;

public abstract class GImage implements IValueRaster {
   private final int width;
   private final int height;

   public GImage(int width, int height) {
      this.height = height;
      this.width = width;
   }

   @Override
   public int getWidth() {
      return this.width;
   }

   @Override
   public int getHeight() {
      return this.height;
   }

   public Dimension getSize() {
      return new Dimension(this.width, this.height);
   }

   @Override
   public final int getValueAt(int x, int y) {
      return this.get(x, y);
   }

   public abstract int get(int var1, int var2);

   public abstract void set(int var1, int var2, int var3);

   public abstract void paint(Graphics var1, int var2, int var3);

   public abstract int getNormalizingFactor();

   public abstract int getMaxPossibleValue();

   public void invert() {
      int maxPossible = this.getMaxPossibleValue();

      for (int y = 0; y < this.height; y++) {
         for (int x = 0; x < this.width; x++) {
            this.set(x, y, maxPossible - this.get(x, y));
         }
      }
   }

   public MemoryImageSource getMemoryImageSource() {
      int[] newPixels = new int[this.width * this.height];
      int normalize = this.getNormalizingFactor();

      for (int y = 0; y < this.height; y++) {
         for (int x = 0; x < this.width; x++) {
            int value = this.get(x, y) * normalize;
            newPixels[y * this.width + x] = 0xFF000000 | (value & 0xFF) << 16 | (value & 0xFF) << 8 | value & 0xFF;
         }
      }

      return new MemoryImageSource(this.width, this.height, newPixels, 0, this.width);
   }

   public MemoryImageSource getMemoryImageSource(Color bg, Color fg) {
      int bgRed = bg.getRed();
      int bgGreen = bg.getGreen();
      int bgBlue = bg.getBlue();
      int fgRed = fg.getRed();
      int fgGreen = fg.getGreen();
      int fgBlue = fg.getBlue();
      double dRed = bgRed - fgRed;
      double dGreen = bgGreen - fgGreen;
      double dBlue = bgBlue - fgBlue;
      int[] newPixels = new int[this.width * this.height];
      int normalize = this.getNormalizingFactor();

      for (int y = 0; y < this.height; y++) {
         for (int x = 0; x < this.width; x++) {
            double p = (double)(this.get(x, y) * normalize) / 255.0;
            int red = (int)(p * dRed + (double)fgRed);
            int green = (int)(p * dGreen + (double)fgGreen);
            int blue = (int)(p * dBlue + (double)fgBlue);
            newPixels[y * this.width + x] = 0xFF000000 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
         }
      }

      return new MemoryImageSource(this.width, this.height, newPixels, 0, this.width);
   }

   public int[][] getPixels() {
      int normalize = this.getNormalizingFactor();
      int[][] p = new int[this.width][this.height];

      for (int y = 0; y < this.height; y++) {
         for (int x = 0; x < this.width; x++) {
            p[x][y] = this.get(x, y) * normalize;
         }
      }

      return p;
   }

   protected abstract GImage createGImage(int var1, int var2);

   public abstract GImage getClone();

   public GImage rotate(Rotation direction) {
      if (direction == Rotation.UPSIDE_DOWN) {
         GImage g = this.createGImage(this.width, this.height);

         for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
               g.set(this.width - x - 1, this.height - y - 1, this.get(x, y));
            }
         }

         return g;
      } else if (direction == Rotation.LEFT) {
         GImage g = this.createGImage(this.height, this.width);

         for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
               g.set(y, this.width - x - 1, this.get(x, y));
            }
         }

         return g;
      } else if (direction != Rotation.RIGHT) {
         if (direction == Rotation.NONE) {
            return this;
         } else {
            throw new RuntimeException();
         }
      } else {
         GImage g = this.createGImage(this.height, this.width);

         for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
               g.set(this.height - y - 1, x, this.get(x, y));
            }
         }

         return g;
      }
   }
}
