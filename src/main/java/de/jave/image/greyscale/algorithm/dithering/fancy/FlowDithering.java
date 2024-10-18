package de.jave.image.greyscale.algorithm.dithering.fancy;

import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.greyscale.algorithm.dithering.IMonochromeDithering;
import de.jave.image.monochrome.GMonochromeImage;
import de.jave.lib.area.BooleanArea;
import java.awt.Point;
import java.util.Random;

public abstract class FlowDithering implements IMonochromeDithering {
   protected int[] dxs;
   protected int[] dys;
   private double[][] weights;
   private Random random = new Random();

   protected FlowDithering(int[] dxs, int[] dys, double[][] weights) {
      this.weights = weights;
      this.dys = dys;
      this.dxs = dxs;
   }

   @Override
   public GMonochromeImage dither(GGreyscaleImage image) {
      BooleanArea booleanArea = new BooleanArea(image.getSize());
      booleanArea.setAll(false);
      GMonochromeImage result = new GMonochromeImage(image.getSize());
      int pixelsToSet = image.getWidth() * image.getHeight();

      do {
         Point point = this.getRandomUnsetPoint(booleanArea);
         int color = this.getDitheredColor(image, result, booleanArea, point);

         do {
            result.set(point.x, point.y, color);
            booleanArea.set(point.x, point.y, true);
            pixelsToSet--;
            point = this.getNextUnsetPoint(image, result, booleanArea, color, point);
         } while (point != null);

         System.err.println(pixelsToSet);
      } while (pixelsToSet > 0);

      return result;
   }

   private Point getRandomUnsetPoint(BooleanArea booleanArea) {
      Point point = null;

      do {
         int x = this.random.nextInt(booleanArea.getWidth());
         int y = this.random.nextInt(booleanArea.getHeight());
         point = new Point(x, y);
      } while (booleanArea.isSet(point));

      return point;
   }

   protected Point getNextUnsetPoint(GGreyscaleImage image, GMonochromeImage result, BooleanArea booleanArea, int color, Point point) {
      for (int i = 0; i < this.dxs.length; i++) {
         Point newPoint = new Point(point.x + this.dxs[i], point.y + this.dys[i]);
         if (image.contains(newPoint.x, newPoint.y) && !booleanArea.isSet(newPoint) && this.getDitheredColor(image, result, booleanArea, newPoint) == color) {
            return newPoint;
         }
      }

      return null;
   }

   private int getDitheredColor(GGreyscaleImage image, GMonochromeImage result, BooleanArea booleanArea, Point point) {
      int median = (image.getMaxValue() + image.getMinValue()) / 2;
      double error = 0.0;

      for (int y = 0; y < this.weights.length; y++) {
         for (int x = 0; x < this.weights[0].length; x++) {
            int dx = x - this.weights[0].length / 2;
            int dy = y - this.weights.length / 2;
            Point newPoint = new Point(point.x + dx, point.y + dy);
            if (image.contains(newPoint.x, newPoint.y) && booleanArea.isSet(newPoint)) {
               error += this.weights[y][x] * (double)result.get(newPoint.x, newPoint.y) * 255.0 - (double)image.get(newPoint.x, newPoint.y);
            }
         }
      }

      int threshold = (int)Math.round((double)median + error);
      return image.get(point.x, point.y) > threshold ? 1 : 0;
   }
}
