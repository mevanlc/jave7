package de.jave.image.greyscale.algorithm.dithering;

public class StevensonArceDithering extends AbstractErrorCorrectedRandomDithering {
   @Override
   public String getName() {
      return "Stevenson Arce";
   }

   @Override
   protected int[][] getMatrix() {
      return new int[][]{{5, 0, 12, 0, 12, 0, 5}, {0, 12, 0, 26, 0, 12, 0}, {16, 0, 30, 0, 26, 0, 12}, {0, 32, 0, 0, 0, 0, 0}};
   }

   @Override
   protected int getMatrixElementSum() {
      return 116;
   }
}
