package de.jave.image.greyscale.algorithm.dithering;

public class FloydSteinbergDithering extends AbstractErrorCorrectedRandomDithering {
   private static final int[][] FLOYD_STEINBERG_MATRIX = new int[][]{{1, 5, 3}, {7, 0, 0}};

   @Override
   protected int getMatrixElementSum() {
      return 16;
   }

   @Override
   public String getName() {
      return "Floyd Steinberg";
   }

   @Override
   protected int[][] getMatrix() {
      return FLOYD_STEINBERG_MATRIX;
   }
}
