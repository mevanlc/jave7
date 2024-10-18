package de.jave.image.greyscale.algorithm.dithering.fancy;

import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.monochrome.GMonochromeImage;
import de.jave.lib.area.BooleanArea;
import java.awt.Point;

public class FancyDithering extends FlowDithering {
   private static final int[] DXS = new int[]{-1, 0, 1, -1, 1, -1, 0, 1};
   private static final int[] DYS = new int[]{1, -1, 0, 0, 1, -1, 1, -1};
   private static final double[][] WEIGHTS = new double[][]{
      {0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 0.8, 1.0, 0.8, 0.0}, {0.0, 1.0, 0.0, 1.0, 0.0}, {0.0, 0.8, 1.0, 0.8, 0.0}, {0.0, 0.0, 0.0, 0.0, 0.0}
   };

   public FancyDithering() {
      super(DXS, DYS, WEIGHTS);
   }

   @Override
   public String getName() {
      return "FancyDithering";
   }

   @Override
   protected Point getNextUnsetPoint(GGreyscaleImage image, GMonochromeImage result, BooleanArea booleanArea, int color, Point point) {
      int bestIndex = -1;
      int minDistance = 255;

      for (int i = 0; i < this.dxs.length; i++) {
         Point newPoint = new Point(point.x + this.dxs[i], point.y + this.dys[i]);
         if (image.contains(newPoint.x, newPoint.y) && !booleanArea.isSet(newPoint)) {
            int distance = Math.abs(image.get(newPoint.x, newPoint.y) - image.get(point.x, point.y));
            if (distance < minDistance) {
               bestIndex = i;
               minDistance = distance;
            }
         }
      }

      return bestIndex != -1 ? new Point(point.x + this.dxs[bestIndex], point.y + this.dys[bestIndex]) : null;
   }
}
