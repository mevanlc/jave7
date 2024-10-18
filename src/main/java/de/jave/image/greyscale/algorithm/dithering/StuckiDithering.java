package de.jave.image.greyscale.algorithm.dithering;

public class StuckiDithering extends AbstractErrorCorrectedRandomDithering {
   @Override
   public String getName() {
      return "Stucki";
   }

   @Override
   protected int[][] getMatrix() {
      return new int[][]{{1, 2, 3, 2, 1}, {2, 4, 8, 4, 2}, {4, 8, 0, 0, 0}};
   }

   @Override
   protected int getMatrixElementSum() {
      return 41;
   }
}
