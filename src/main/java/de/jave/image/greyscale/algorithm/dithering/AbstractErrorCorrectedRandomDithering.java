package de.jave.image.greyscale.algorithm.dithering;

import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.greyscale.algorithm.dithering.util.ValueSegmentation;
import de.jave.image.monochrome.GMonochromeImage;

public abstract class AbstractErrorCorrectedRandomDithering extends AbstractRandomDithering {
   @Override
   public final GMonochromeImage dither(GGreyscaleImage image) {
      this.seed(image);
      int imageHeight = image.getHeight();
      int imageWidth = image.getWidth();
      GMonochromeImage g = new GMonochromeImage(imageWidth, imageHeight);
      int matrixHeight = this.getMatrixHeight();
      int matrixWidth = this.getMatrixWidth();
      int matrixElementSum = this.getMatrixElementSum();
      int errorLinesHeight = matrixHeight + 1;
      int[][] lastLineErrors = new int[imageWidth + matrixWidth - 1][errorLinesHeight];
      int[][] matrix = this.getMatrix();

      for (int y = 0; y < imageHeight; y++) {
         int newErrorLineIndex = (y + matrixHeight) % errorLinesHeight;

         for (int x = 0; x < imageWidth; x++) {
            double oldError = 0.0;

            for (int yy = 0; yy < matrixHeight; yy++) {
               int lineIndex = (y + yy + 1) % errorLinesHeight;

               for (int xx = 0; xx < matrixWidth; xx++) {
                  oldError += (double)matrix[yy][xx] / (double)matrixElementSum * (double)lastLineErrors[x + xx][lineIndex];
               }
            }

            int newValue = 0;
            if ((int)((double)image.get(x, y) - oldError) + 64 - this.random(128) >= 128) {
               newValue = 1;
            }

            g.set(x, y, newValue);
            lastLineErrors[x + matrixWidth / 2][newErrorLineIndex] = newValue * 255 - image.get(x, y);
         }
      }

      return g;
   }

   @Override
   public final GGreyscaleImage dither(GGreyscaleImage image, int depth) {
      this.seed(image);
      int imageHeight = image.getHeight();
      int imageWidth = image.getWidth();
      GGreyscaleImage g = new GGreyscaleImage(imageWidth, imageHeight);
      int matrixHeight = this.getMatrixHeight();
      int matrixWidth = this.getMatrixWidth();
      int matrixElementSum = this.getMatrixElementSum();
      int errorLinesHeight = matrixHeight + 1;
      int[][] lastLineErrors = new int[imageWidth + matrixWidth - 1][errorLinesHeight];
      int[][] matrix = this.getMatrix();
      ValueSegmentation segmentation = new ValueSegmentation(0, 255, depth);

      for (int y = 0; y < imageHeight; y++) {
         int newErrorLineIndex = (y + matrixHeight) % errorLinesHeight;

         for (int x = 0; x < imageWidth; x++) {
            double oldError = 0.0;

            for (int yy = 0; yy < matrixHeight; yy++) {
               int lineIndex = (y + yy + 1) % errorLinesHeight;

               for (int xx = 0; xx < matrixWidth; xx++) {
                  oldError += (double)matrix[yy][xx] / (double)matrixElementSum * (double)lastLineErrors[x + xx][lineIndex];
               }
            }

            int newValue = segmentation.getSegmentValue((int)((double)image.get(x, y) - oldError) + 256 / depth / 2 - this.random(256 / depth));
            g.set(x, y, newValue);
            lastLineErrors[x + this.getMatrixWidth() / 2][newErrorLineIndex] = newValue - image.get(x, y);
         }
      }

      return g;
   }

   private int getMatrixHeight() {
      return this.getMatrix().length;
   }

   private int getMatrixWidth() {
      return this.getMatrix()[0].length;
   }

   protected abstract int[][] getMatrix();

   protected abstract int getMatrixElementSum();
}
