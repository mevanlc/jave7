package de.jave.image.greyscale.algorithm.dithering;

public class DitheringAlgorithms {
   public static IGreyscaleDithering[] getAllRandomDitheringAlgorithms() {
      return new IGreyscaleDithering[]{
         new SimpleRandomDithering(),
         new FloydSteinbergDithering(),
         new StevensonArceDithering(),
         new StuckiDithering(),
         new JarvisJudiceNinkeDithering(),
         new SierraDithering()
      };
   }

   public static IGreyscaleDithering[] getAllGreyscaleDitheringAlgorithms() {
      return new IGreyscaleDithering[]{
         new ThresholdDithering(),
         new SimpleRandomDithering(),
         new FloydSteinbergDithering(),
         new StevensonArceDithering(),
         new StuckiDithering(),
         new JarvisJudiceNinkeDithering(),
         new SierraDithering()
      };
   }
}
