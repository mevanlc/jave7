package de.jave.image.greyscale.algorithm.dithering.fancy;

public class DiagonalDithering extends FlowDithering {
   private static final int[] DXS = new int[]{-1, -1, 0, 1, 1, -1, 0, 1};
   private static final int[] DYS = new int[]{1, 0, 1, 0, 1, -1, -1, -1};
   private static final double[][] WEIGHTS = new double[][]{
      {0.0, 0.8, 1.2, 0.8, 0.0}, {0.8, 0.2, 0.0, 0.2, 0.8}, {1.2, 0.0, 0.0, 0.0, 1.2}, {0.8, 0.2, 0.0, 0.2, 0.8}, {0.0, 0.8, 1.2, 0.8, 0.0}
   };

   public DiagonalDithering() {
      super(DXS, DYS, WEIGHTS);
   }

   @Override
   public String getName() {
      return "DiagonalDithering (experimental)";
   }
}
