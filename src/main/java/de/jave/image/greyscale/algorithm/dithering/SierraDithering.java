package de.jave.image.greyscale.algorithm.dithering;

public class SierraDithering extends AbstractErrorCorrectedRandomDithering {
   @Override
   protected int[][] getMatrix() {
      return new int[][]{{0, 2, 3, 2, 0}, {2, 4, 5, 4, 2}, {3, 5, 0, 0, 0}};
   }

   @Override
   protected int getMatrixElementSum() {
      return 25;
   }

   @Override
   public String getName() {
      return "Sierra";
   }
}
