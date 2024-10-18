package de.jave.image.greyscale.algorithm.dithering;

public class HearttoneDithering extends AbstractMatrixDithering {
   private static final int[][] HEARTTONE_MATRIX = new int[][]{
      {5, 24, 51, 51, 24, 28, 5, 5},
      {24, 83, 148, 149, 85, 28, 28, 5},
      {51, 148, 220, 224, 169, 87, 28, 28},
      {54, 155, 231, 249, 229, 167, 81, 22},
      {43, 83, 148, 251, 247, 206, 119, 37},
      {54, 155, 231, 249, 229, 167, 81, 22},
      {52, 151, 226, 230, 172, 88, 28, 28},
      {26, 102, 197, 197, 104, 30, 28, 5}
   };

   @Override
   protected int getMaxMatrixValue() {
      return 255;
   }

   @Override
   public String getName() {
      return "Hearttone";
   }

   @Override
   protected int[][] getMatrix() {
      return HEARTTONE_MATRIX;
   }
}
