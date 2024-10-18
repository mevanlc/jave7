package de.jave.image.greyscale.algorithm.dithering;

public class HalftoneDithering extends AbstractMatrixDithering {
   private static final int[][] HALFTONE_MATRIX = new int[][]{
      {19, 25, 23, 17, 14, 8, 10, 16},
      {21, 31, 29, 27, 12, 2, 4, 6},
      {28, 30, 32, 22, 5, 3, 1, 11},
      {18, 24, 26, 20, 15, 9, 7, 13},
      {14, 8, 10, 16, 19, 25, 23, 17},
      {12, 2, 4, 6, 21, 31, 29, 27},
      {5, 3, 1, 11, 28, 30, 32, 22},
      {15, 9, 7, 13, 18, 24, 26, 20}
   };

   @Override
   protected int getMaxMatrixValue() {
      return 32;
   }

   @Override
   public String getName() {
      return "Halftone";
   }

   @Override
   protected int[][] getMatrix() {
      return HALFTONE_MATRIX;
   }
}
