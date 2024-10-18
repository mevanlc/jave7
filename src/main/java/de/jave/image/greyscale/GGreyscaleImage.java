package de.jave.image.greyscale;

import de.jave.image.GImage;
import de.jave.image.monochrome.GMonochromeImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.ProgressUtilities;

public class GGreyscaleImage extends GImage {
   private int[][] pixels;
   private int minValue;
   private int maxValue;
   private boolean minMaxValid;
   private static final int[] INV_MASK = new int[]{-256, -65281, -16711681, 16777215};
   public static final int[][] MATRIX_UNSHARPEN = new int[][]{{1, 2, 1}, {2, 3, 2}, {1, 2, 1}};
   public static final int[][] MATRIX_SHARPEN_MOST = new int[][]{{-1, -2, -1}, {-2, 13, -2}, {-1, -2, -1}};
   public static final int[][] MATRIX_SHARPEN_MORE = new int[][]{{-1, -1, -1}, {-1, 9, -1}, {-1, -1, -1}};
   public static final int[][] MATRIX_SHARPEN_BIG = new int[][]{
      {-1, -1, -1, -1, -1}, {-1, 2, 2, 2, -1}, {-1, 2, 8, 2, -1}, {-1, 2, 2, 2, -1}, {-1, -1, -1, -1, -1}
   };
   public static final int[][] MATRIX_LAPLACE = new int[][]{{1, 0, 1}, {0, -4, 0}, {1, 0, 1}};
   public static final int[][] MATRIX_OUTLINE = new int[][]{
      {1, 1, 1, 1, 1, 1, 1},
      {1, 0, 0, 0, 0, 0, 1},
      {1, 0, 0, 0, 0, 0, 1},
      {1, 0, 0, -23, 0, 0, 1},
      {1, 0, 0, 0, 0, 0, 1},
      {1, 0, 0, 0, 0, 0, 1},
      {1, 1, 1, 1, 1, 1, 1}
   };
   protected static Color[] greyscaleColors = null;

   public GGreyscaleImage(int width, int height) {
      super(width, height);
      this.pixels = new int[height][(width + 3) / 4];
      this.minValue = 0;
      this.maxValue = 0;
      this.minMaxValid = true;
   }

   public GGreyscaleImage(int[][] pixels) {
      super(pixels.length, pixels[0].length);
      this.minValue = 255;
      this.maxValue = 0;
      this.pixels = new int[this.getHeight()][(this.getWidth() + 3) / 4];

      for (int y = 0; y < this.getHeight(); y++) {
         for (int x = 0; x < this.getWidth(); x++) {
            this.set(x, y, pixels[x][y]);
            if (pixels[x][y] < this.minValue) {
               this.minValue = pixels[x][y];
            }

            if (pixels[x][y] > this.maxValue) {
               this.maxValue = pixels[x][y];
            }
         }
      }

      this.minMaxValid = true;
   }

   @Override
   public final void set(int x, int y, int value) {
      this.minMaxValid = false;
      this.pixels[y][x / 4] = this.pixels[y][x / 4] & INV_MASK[x % 4];
      this.pixels[y][x / 4] = this.pixels[y][x / 4] | value << 8 * (x % 4);
   }

   @Override
   public final int get(int x, int y) {
      return this.pixels[y][x / 4] >> 8 * (x % 4) & 0xFF;
   }

   @Override
   public int getNormalizingFactor() {
      return 1;
   }

   @Override
   public int getMaxPossibleValue() {
      return 255;
   }

   public void normalize() {
      int min = this.getMinValue();
      int max = this.getMaxValue();
      if (min == max) {
         max++;
      }

      for (int y = 0; y < this.getHeight(); y++) {
         for (int x = 0; x < this.getWidth(); x++) {
            this.set(x, y, (int)Math.round(255.0 * (double)(this.get(x, y) - min) / (double)(max - min)));
         }
      }

      this.minValue = 0;
      this.maxValue = 255;
      this.minMaxValid = true;
   }

   public GGreyscaleImage convert(boolean normalize, boolean invert, double gamma, double highlight, double shadow) {
      if (this.pixels == null) {
         return null;
      } else {
         int min = 0;
         int max = 255;
         if (normalize) {
            min = this.getMinValue();
            max = this.getMaxValue();
         }

         double gamma_1 = 1.0 / gamma;
         GGreyscaleImage g = new GGreyscaleImage(this.getWidth(), this.getHeight());

         for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
               double value = (double)(this.get(x, y) - min) / (double)(max - min);
               if (invert) {
                  value = 1.0 - value;
               }

               value = Math.min(value * 100.0 / highlight, 1.0);
               value = Math.max((value - shadow / 100.0) * (100.0 / (100.0 - shadow)), 0.0);
               value = Math.pow(value, gamma_1);
               g.set(x, y, (int)(255.0 * value));
            }
         }

         return g;
      }
   }

   public GGreyscaleImage getXScaledInstance(int newWidth) {
      double scaleX = (double)newWidth / (double)this.getWidth();
      GGreyscaleImage g = new GGreyscaleImage(newWidth, this.getHeight());
      if (scaleX >= 1.0) {
         for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < newWidth; x++) {
               g.set(x, y, this.getValueAt((double)x / scaleX, y));
            }
         }
      } else {
         for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < newWidth; x++) {
               g.set(x, y, this.getValueAt((double)x / scaleX, ((double)x + 1.0) / scaleX, y));
            }
         }
      }

      return g;
   }

   public GGreyscaleImage getYScaledInstance(int newHeight) {
      double scaleY = (double)newHeight / (double)this.getHeight();
      GGreyscaleImage g = new GGreyscaleImage(this.getWidth(), newHeight);
      if (scaleY >= 1.0) {
         for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < this.getWidth(); x++) {
               g.set(x, y, this.getValueAt(x, (double)y / scaleY));
            }
         }
      } else {
         for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < this.getWidth(); x++) {
               g.set(x, y, this.getValueAt(x, (double)y / scaleY, ((double)y + 1.0) / scaleY));
            }
         }
      }

      return g;
   }

   public int getMinValue() {
      if (!this.minMaxValid) {
         this.determineValues();
      }

      return this.minValue;
   }

   public int getMaxValue() {
      if (!this.minMaxValid) {
         this.determineValues();
      }

      return this.maxValue;
   }

   protected void determineValues() {
      this.minValue = this.get(0, 0);
      this.maxValue = this.get(0, 0);

      for (int y = 0; y < this.getHeight(); y++) {
         for (int x = 0; x < this.getWidth(); x++) {
            int value = this.get(x, y);
            if (value > this.maxValue) {
               this.maxValue = value;
            }

            if (value < this.minValue) {
               this.minValue = value;
            }
         }
      }

      this.minMaxValid = true;
   }

   protected GGreyscaleImage createGImage(int width, int height) {
      return new GGreyscaleImage(width, height);
   }

   public GGreyscaleImage getClone() {
      int w = (this.getWidth() + 3) / 4;
      int[][] p = new int[this.getHeight()][w];

      for (int y = 0; y < this.getHeight(); y++) {
         System.arraycopy(this.pixels[y], 0, p[y], 0, w);
      }

      GGreyscaleImage g = new GGreyscaleImage(this.getWidth(), this.getHeight());
      g.pixels = p;
      g.minValue = this.minValue;
      g.maxValue = this.maxValue;
      g.minMaxValid = this.minMaxValid;
      return g;
   }

   public GGreyscaleImage sharpenMore() {
      return this.convolve(MATRIX_SHARPEN_MORE, 1.0, 0.0);
   }

   public GGreyscaleImage sharpenBig() {
      return this.convolve(MATRIX_SHARPEN_BIG, 8.0, 0.0);
   }

   public GGreyscaleImage filterLaplace() {
      return this.convolve(MATRIX_LAPLACE, 1.0, 0.0);
   }

   public GGreyscaleImage filterOutline() {
      return this.convolve(MATRIX_OUTLINE, 1.0, 0.0);
   }

   public GGreyscaleImage filterOutline(double amount) {
      return amount == 0.0 ? this : this.convolve(MATRIX_OUTLINE, 1.0, 1.0 - amount);
   }

   public GGreyscaleImage sharpen() {
      return this.convolve(MATRIX_SHARPEN_MOST, 1.0, 0.0);
   }

   public GGreyscaleImage sharpen(double amount) {
      return amount == 0.0 ? this : this.convolve(MATRIX_SHARPEN_MOST, 1.0, 1.0 - amount);
   }

   public GGreyscaleImage unsharpen(double amount) {
      return this.convolve(MATRIX_UNSHARPEN, 15.0, 1.0 - amount);
   }

   public void brighten(double value) {
      if (value != 0.0) {
         double gamma_1 = 1.0;
         if (++value < 0.1) {
            value = 0.1;
         }

         gamma_1 = 1.0 / value;

         for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
               int newValue = (int)Math.pow(this.get(x, y), gamma_1);
               if (newValue > 255) {
                  newValue = 255;
               }

               this.set(x, y, newValue);
            }
         }
      }
   }

   public GMonochromeImage getThresholdedInstance(int thresh) {
      int height = this.getHeight();
      int width = this.getWidth();
      GMonochromeImage monochromeImage = new GMonochromeImage(width, height);

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            if (this.get(x, y) >= thresh) {
               monochromeImage.set(x, y, 1);
            }
         }
      }

      return monochromeImage;
   }

   public int countPixels(int value) {
      int result = 0;

      for (int y = 0; y < this.getHeight(); y++) {
         for (int x = 0; x < this.getWidth(); x++) {
            if (this.get(x, y) == value) {
               result++;
            }
         }
      }

      return result;
   }

   public int countPixelsWithMaxValue(int value) {
      int result = 0;

      for (int y = 0; y < this.getHeight(); y++) {
         for (int x = 0; x < this.getWidth(); x++) {
            if (this.get(x, y) <= value) {
               result++;
            }
         }
      }

      return result;
   }

   public GMonochromeImage getThresholdedInstance() {
      int max = this.getMaxValue();
      int min = this.getMinValue();
      return this.getThresholdedInstance((max + min) / 2);
   }

   public void applyHysteresisThreshold(int low, int high, ICancelable cancelable) throws InterruptedException {
      int height = this.getHeight();
      int width = this.getWidth();

      for (int y = 0; y < height; y++) {
         ProgressUtilities.checkInterrupted(cancelable);

         for (int x = 0; x < width; x++) {
            if (this.get(x, y) > high) {
               this.set(x, y, 255);
            } else if (this.get(x, y) < low) {
               this.set(x, y, 0);
            } else {
               this.set(x, y, 127);
            }
         }
      }

      boolean done = false;

      while (!done) {
         done = true;

         for (int y = 0; y < height; y++) {
            ProgressUtilities.checkInterrupted(cancelable);

            for (int xx = 0; xx < width; xx++) {
               if (this.get(xx, y) == 127) {
                  if ((xx <= 0 || this.get(xx - 1, y) != 255)
                     && (y <= 0 || this.get(xx, y - 1) != 255)
                     && (xx <= 0 || y <= 0 || this.get(xx - 1, y - 1) != 255)
                     && (xx <= 0 || y + 1 >= height || this.get(xx - 1, y + 1) != 255)
                     && (xx + 1 >= width || y <= 0 || this.get(xx + 1, y - 1) != 255)
                     && (xx + 1 >= width || this.get(xx + 1, y) != 255)
                     && (y + 1 >= height || this.get(xx, y + 1) != 255)
                     && (y + 1 >= height || xx + 1 >= width || this.get(xx + 1, y + 1) != 255)) {
                     done = false;
                  } else {
                     this.set(xx, y, 255);
                  }
               }
            }
         }
      }

      for (int y = 0; y < height; y++) {
         ProgressUtilities.checkInterrupted(cancelable);

         for (int xxx = 0; xxx < width; xxx++) {
            if (this.get(xxx, y) == 127) {
               this.set(xxx, y, 0);
            }
         }
      }

      this.minValue = 0;
      this.maxValue = 255;
      this.minMaxValid = true;
   }

   public GGreyscaleImage convolve(int[][] matrix, double divide, double bias) {
      int width = this.getWidth();
      int height = this.getHeight();
      GGreyscaleImage g = new GGreyscaleImage(width, height);
      int wm = matrix.length;
      int wh = matrix[0].length;
      int wm2 = wm / 2;
      int wh2 = wh / 2;

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            int sum = 0;

            for (int dx = -wm2; dx <= wm2; dx++) {
               for (int dy = -wh2; dy <= wh2; dy++) {
                  int xx = x + dx;
                  if (xx < 0) {
                     xx = 0;
                  } else if (xx >= width) {
                     xx = width - 1;
                  }

                  int yy = y + dy;
                  if (yy < 0) {
                     yy = 0;
                  } else if (yy >= height) {
                     yy = height - 1;
                  }

                  int t1 = this.get(xx, yy);
                  int t2 = matrix[wm2 + dx][wh2 + dy];
                  sum += t1 * t2;
               }
            }

            int value = (int)(bias * (double)this.get(x, y) + (1.0 - bias) * (double)sum / divide);
            if (value > 255) {
               g.set(x, y, 255);
            } else if (value < 0) {
               g.set(x, y, 0);
            } else {
               g.set(x, y, value);
            }
         }
      }

      return g;
   }

   public GGreyscaleImage getSubImage(Rectangle area) {
      GGreyscaleImage g = new GGreyscaleImage(area.width, area.height);

      for (int y = 0; y < area.height; y++) {
         for (int x = 0; x < area.width; x++) {
            int x0 = x + area.x;
            int y0 = y + area.y;
            if (this.contains(x0, y0)) {
               g.set(x, y, this.get(x0, y0));
            } else {
               g.set(x, y, 0);
            }
         }
      }

      return g;
   }

   public boolean contains(int x, int y) {
      return x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight();
   }

   public GGreyscaleImage getScaledInstance(int newWidth, int newHeight) {
      double scaleX = (double)newWidth / (double)this.getWidth();
      double scaleY = (double)newHeight / (double)this.getHeight();
      if (scaleX >= scaleY) {
         GGreyscaleImage g1 = this.getXScaledInstance(newWidth);
         return g1.getYScaledInstance(newHeight);
      } else {
         GGreyscaleImage g1 = this.getYScaledInstance(newHeight);
         return g1.getXScaledInstance(newWidth);
      }
   }

   public GGreyscaleImage getXScaledInstance(double scaleX) {
      int newWidth = (int)((double)this.getWidth() * scaleX);
      return this.getXScaledInstance(newWidth);
   }

   public GGreyscaleImage getYScaledInstance(double scaleY) {
      int newHeight = (int)((double)this.getHeight() * scaleY);
      return this.getYScaledInstance(newHeight);
   }

   public GGreyscaleImage getScaledInstance(double scaleX, double scaleY) {
      int newWidth = (int)((double)this.getWidth() * scaleX);
      int newHeight = (int)((double)this.getHeight() * scaleY);
      if (scaleX >= scaleY) {
         GGreyscaleImage g1 = this.getXScaledInstance(newWidth);
         return g1.getYScaledInstance(newHeight);
      } else {
         GGreyscaleImage g1 = this.getYScaledInstance(newHeight);
         return g1.getXScaledInstance(newWidth);
      }
   }

   public int getValueAt(double x1, double x2, int y) {
      int minY = 0;
      int maxY = this.getHeight() - 1;
      if (y <= maxY && y >= 0) {
         double minX = 0.0;
         double maxX = this.getWidth();
         if (!(x2 < 0.0) && !(x1 > maxX)) {
            if (x1 < 0.0) {
               x1 = 0.0;
            }

            if (x2 > maxX) {
               x2 = maxX;
            }

            double sum = 0.0;
            int xA = (int)Math.ceil(x1);
            int xB = (int)Math.floor(x2);

            for (int x = xA; x < xB; x++) {
               sum += this.get(x, y);
            }

            if ((double)xA > x1) {
               sum += ((double)xA - x1) * (double)this.get((int)Math.floor(x1), y);
            }

            if ((double)xB < x2) {
               sum += (x2 - (double)xB) * (double)this.get(xB, y);
            }

            return (int)(sum / (x2 - x1));
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   public int getValueAt(int x, double y1, double y2) {
      int minX = 0;
      int maxX = this.getWidth() - 1;
      if (x <= maxX && x >= 0) {
         double minY = 0.0;
         double maxY = this.getHeight();
         if (!(y2 < 0.0) && !(y1 > maxY)) {
            if (y1 < 0.0) {
               y1 = 0.0;
            }

            if (y2 > maxY) {
               y2 = maxY;
            }

            double sum = 0.0;
            int yA = (int)Math.ceil(y1);
            int yB = (int)Math.floor(y2);

            for (int y = yA; y < yB; y++) {
               sum += this.get(x, y);
            }

            if ((double)yA > y1) {
               sum += ((double)yA - y1) * (double)this.get(x, (int)Math.floor(y1));
            }

            if ((double)yB < y2) {
               sum += (y2 - (double)yB) * (double)this.get(x, yB);
            }

            return (int)(sum / (y2 - y1));
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   public int getValueAt(double x, int y0) {
      if (x > (double)(this.getWidth() - 1)) {
         x = this.getWidth() - 1;
      }

      int x0 = (int)x;
      double tx = x - (double)x0;
      return tx == 0.0 ? this.get(x0, y0) : (int)((double)this.get(x0, y0) * (1.0 - tx) + (double)this.get(x0 + 1, y0) * tx);
   }

   public int getValueAt(int x0, double y) {
      if (y > (double)(this.getHeight() - 1)) {
         y = this.getHeight() - 1;
      }

      int y0 = (int)y;
      double ty = y - (double)y0;
      return ty == 0.0 ? this.get(x0, y0) : (int)((double)this.get(x0, y0) * (1.0 - ty) + (double)this.get(x0, y0 + 1) * ty);
   }

   public int getValueAt(double x, double y) {
      if (x < 0.0 || y < 0.0) {
         return 0;
      } else if (!(x >= (double)this.getWidth()) && !(y >= (double)this.getHeight())) {
         if (x > (double)(this.getWidth() - 1)) {
            x = this.getWidth() - 1;
         }

         if (y > (double)(this.getHeight() - 1)) {
            y = this.getHeight() - 1;
         }

         int x0 = (int)x;
         int y0 = (int)y;
         double tx = x - (double)x0;
         double ty = y - (double)y0;
         if (tx == 0.0 && ty == 0.0) {
            return this.get(x0, y0);
         } else if (tx == 0.0) {
            return (int)((double)this.get(x0, y0) * (1.0 - ty) + (double)this.get(x0, y0 + 1) * ty);
         } else {
            return ty == 0.0
               ? (int)((double)this.get(x0, y0) * (1.0 - tx) + (double)this.get(x0 + 1, y0) * tx)
               : (int)(
                  (1.0 - ty) * ((double)this.get(x0, y0) * (1.0 - tx) + (double)this.get(x0 + 1, y0) * tx)
                     + ty * ((double)this.get(x0, y0 + 1) * (1.0 - tx) + (double)this.get(x0 + 1, y0 + 1) * tx)
               );
         }
      } else {
         return 0;
      }
   }

   @Override
   public void paint(Graphics g, int x0, int y0) {
      if (this.pixels != null) {
         if (greyscaleColors == null) {
            greyscaleColors = new Color[256];

            for (int i = 0; i < 256; i++) {
               greyscaleColors[i] = new Color(i, i, i);
            }
         }

         int max = this.getMaxValue();
         g.setColor(greyscaleColors[max]);
         g.fillRect(x0, x0, this.getWidth(), this.getHeight());

         for (int y = this.getHeight() - 1; y >= 0; y--) {
            for (int x = this.getWidth() - 1; x >= 0; x--) {
               int value = this.get(x, y);
               if (value != max) {
                  g.setColor(greyscaleColors[value]);
                  g.drawLine(x0 + x, y0 + y, x0 + x, y0 + y);
               }
            }
         }
      }
   }
}
