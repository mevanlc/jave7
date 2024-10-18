package de.jave.image.greyscale.algorithm.dithering;

public class JarvisJudiceNinkeDithering extends AbstractErrorCorrectedRandomDithering {
   @Override
   public String getName() {
      return "Jarvis Judice Ninke";
   }

   @Override
   protected int[][] getMatrix() {
      return new int[][]{{1, 3, 5, 3, 1}, {3, 5, 7, 5, 3}, {5, 7, 0, 0, 0}};
   }

   @Override
   protected int getMatrixElementSum() {
      return 48;
   }
}
