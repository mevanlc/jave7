package de.jave.image.greyscale.algorithm.dithering;

public class Bayer4Dithering extends AbstractMatrixDithering {
   private static final int[][] BAYER_MATRIX4 = new int[][]{{0, 8, 2, 10}, {12, 4, 14, 6}, {3, 11, 1, 9}, {15, 7, 13, 5}};

   @Override
   protected int[][] getMatrix() {
      return BAYER_MATRIX4;
   }

   @Override
   protected int getMaxMatrixValue() {
      return 15;
   }

   @Override
   public String getName() {
      return "Bayer 4";
   }
}
