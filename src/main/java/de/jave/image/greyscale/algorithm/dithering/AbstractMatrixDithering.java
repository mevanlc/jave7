package de.jave.image.greyscale.algorithm.dithering;

import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.greyscale.algorithm.dithering.util.ValueSegmentation;
import de.jave.image.monochrome.GMonochromeImage;

public abstract class AbstractMatrixDithering implements IGreyscaleDithering {
   protected abstract int getMaxMatrixValue();

   protected abstract int[][] getMatrix();

   @Override
   public abstract String getName();

   @Override
   public GMonochromeImage dither(GGreyscaleImage image) {
      double scale = (double)this.getMaxMatrixValue() / 255.0;
      GMonochromeImage g = new GMonochromeImage(image.getWidth(), image.getHeight());

      for (int y = 0; y < image.getHeight(); y++) {
         for (int x = 0; x < image.getWidth(); x++) {
            if ((double)image.get(x, y) * scale >= (double)this.getMatrixValue(x, y)) {
               g.set(x, y, 1);
            } else {
               g.set(x, y, 0);
            }
         }
      }

      return g;
   }

   @Override
   public GGreyscaleImage dither(GGreyscaleImage image, int depth) {
      ValueSegmentation segmentation = new ValueSegmentation(0, 255, depth);
      GGreyscaleImage g = new GGreyscaleImage(image.getWidth(), image.getHeight());

      for (int y = 0; y < image.getHeight(); y++) {
         for (int x = 0; x < image.getWidth(); x++) {
            for (int i = 0; i < depth; i++) {
               if ((double)image.get(x, y) * ((double)i / (double)(depth - 1)) * (double)this.getMaxMatrixValue() / 255.0 >= (double)this.getMatrixValue(x, y)) {
                  g.set(x, y, segmentation.getValueForSegment(i));
                  break;
               }
            }
         }
      }

      return g;
   }

   private int getMatrixValue(int x, int y) {
      int[][] matrix = this.getMatrix();
      int width = matrix[0].length;
      int height = matrix.length;
      return matrix[x % width][y % height];
   }
}
