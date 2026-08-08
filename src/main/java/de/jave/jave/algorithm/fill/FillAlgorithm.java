package de.jave.jave.algorithm.fill;

import de.jave.jave.pattern.Pattern;
import de.jave.lib.CharacterPlate;
import java.awt.Rectangle;

public class FillAlgorithm {
   private static final double[][] BAYER_MATRIX2 = new double[][]{{0.6666666666666666, 0.3333333333333333}, {0.0, 1.0}};
   private static final double RADIUS_CORRECT_FACTOR = 3.5;
   private static final double LINEAR_CORRECT_FACTOR = 3.5;

   public static void fillSolid(CharacterPlate characterPlate, int x, int y, char fillChar, FillMatchMode fillMatchMode) {
      Rectangle bounds = new FillMatcher(fillMatchMode, characterPlate).markAreaForFill(x, y);
      if (bounds != null) {
         int boundsMinX = bounds.x;
         int boundsMaxX = boundsMinX + bounds.width - 1;
         int boundsMinY = bounds.y;
         int boundsMaxY = boundsMinY + bounds.height - 1;

         for (int xx = boundsMinX; xx <= boundsMaxX; xx++) {
            for (int yy = boundsMinY; yy <= boundsMaxY; yy++) {
               if (characterPlate.glyphAt(xx, yy) == 2) {
                  characterPlate.setForce(xx, yy, fillChar);
               }
            }
         }
      }
   }

   public static void fillPattern(CharacterPlate characterPlate, int x, int y, Pattern pattern, FillMatchMode fillMatchMode) {
      if (pattern != null) {
         int[][] fillPattern = pattern.getContent();
         int fillPatternHeight = fillPattern.length;
         if (fillPatternHeight != 0) {
            int fillPatternWidth = fillPattern[0].length;
            if (fillPatternWidth != 0) {
               Rectangle bounds = new FillMatcher(fillMatchMode, characterPlate).markAreaForFill(x, y);
               if (bounds != null) {
                  int boundsMinX = bounds.x;
                  int boundsMaxX = boundsMinX + bounds.width - 1;
                  int boundsMinY = bounds.y;
                  int boundsMaxY = boundsMinY + bounds.height - 1;
                  int pox = 0;

                  while ((pox + x) % fillPatternWidth != 0) {
                     pox++;
                  }

                  int poy = 0;

                  while ((poy + y) % fillPatternHeight != 0) {
                     poy++;
                  }

                  for (int xx = boundsMinX; xx <= boundsMaxX; xx++) {
                     for (int yy = boundsMinY; yy <= boundsMaxY; yy++) {
                        if (characterPlate.glyphAt(xx, yy) == 2) {
                           characterPlate.setForce(xx, yy, fillPattern[(yy + poy) % fillPatternHeight][(xx + pox) % fillPatternWidth]);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static void fillGradient(
      CharacterPlate characterPlate, int x, int y, int x2, int y2, char[] fillGradient, GradientStyle style, FillMatchMode fillMatchMode, boolean dither
   ) {
      int gradientResolution = fillGradient.length;
      Rectangle bounds = new FillMatcher(fillMatchMode, characterPlate).markAreaForFill(x, y);
      if (bounds != null) {
         int boundsMinX = bounds.x;
         int boundsMaxX = boundsMinX + bounds.width - 1;
         int boundsMinY = bounds.y;
         int boundsMaxY = boundsMinY + bounds.height - 1;
         double v1 = x2 - x;
         double v2 = (double)(y2 - y) * 3.5;
         double scale = Math.sqrt(v1 * v1 + v2 * v2);
         v1 /= scale;
         v2 /= scale;
         double n1 = -v2;
         double n2 = v1;
         double min = 0.0;
         double max = 0.0;
         double radius = 0.0;

         for (int xx = boundsMinX; xx <= boundsMaxX; xx++) {
            for (int yy = boundsMinY; yy <= boundsMaxY; yy++) {
               if (characterPlate.glyphAt(xx, yy) == 2) {
                  double d = (double)(y - yy) * n1 - (double)(x - xx) * n2;
                  if (d < min) {
                     min = d;
                  } else if (d > max) {
                     max = d;
                  }

                  double r = (double)((xx - x) * (xx - x)) + 3.5 * (double)(yy - y) * (double)(yy - y);
                  if (r > radius) {
                     radius = r;
                  }
               }
            }
         }

         radius = Math.sqrt(radius);

         for (int yyx = boundsMinY; yyx <= boundsMaxY; yyx++) {
            for (int xx = boundsMinX; xx <= boundsMaxX; xx++) {
               if (characterPlate.glyphAt(xx, yyx) == 2) {
                  double index = 0.0;
                  if (style == GradientStyle.LINEAR) {
                     double dx = (double)(y - yyx) * n1 - (double)(x - xx) * n2;
                     double intensity = (dx - min) / (max - min);
                     index = intensity * (double)gradientResolution - 0.5;
                  } else if (style == GradientStyle.SUNBURST) {
                     double r = Math.sqrt(3.5 * (double)(yyx - y) * (double)(yyx - y) + (double)((xx - x) * (xx - x)));
                     double intensity = r / radius;
                     index = intensity * ((double)gradientResolution - 0.5);
                  } else if (style == GradientStyle.RADIAL) {
                     double w1 = xx - x;
                     double w2 = (double)(yyx - y) * 3.5;
                     double s = Math.sqrt(w1 * w1 + w2 * w2);
                     w1 /= s;
                     w2 /= s;
                     double angle = Math.acos(w1 * v1 + w2 * v2);
                     double intensity = angle / Math.PI;
                     index = intensity * ((double)gradientResolution - 1.0);
                  }

                  if (dither) {
                     index += BAYER_MATRIX2[xx % 2][yyx % 2] - 0.5;
                  }

                  int i = (int)Math.round(index);
                  if (i < 0) {
                     i = 0;
                  } else if (i >= gradientResolution) {
                     i = gradientResolution - 1;
                  }

                  char ch = fillGradient[i];
                  characterPlate.setForce(xx, yyx, ch);
               }
            }
         }
      }
   }
}
