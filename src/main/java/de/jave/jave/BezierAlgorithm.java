package de.jave.jave;

import de.jave.jave.pixelplate.PixelPlate;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class BezierAlgorithm {
   protected static final double STD_EPSILON = 2.5;
   protected static final int MAX_DEPTH = 7;

   public static Point[] getPolyLineForBezier(Point[] bezierPoints) {
      return getPolyLineForBezier(bezierPoints, 1.0);
   }

   public static Point[] getPolyLineForBezier(Point[] bezierPoints, double epsilon) {
      Point2d[] points = new Point2d[bezierPoints.length];

      for (int i = 0; i < bezierPoints.length; i++) {
         points[i] = new Point2d(bezierPoints[i]);
      }

      List<Point2d> v = new ArrayList<>(64);
      v.add(points[0]);
      internalBezier(v, points, 7, 2.5 * epsilon);
      Point[] result = new Point[v.size()];

      for (int i = 0; i < result.length; i++) {
         Point2d p = v.get(i);
         result[i] = new Point((int)Math.round(p.getX()), (int)Math.round(p.getY()));
      }

      return result;
   }

   public static Point2d[] getPolyLineForBezier(Point2d[] bezierPoints) {
      return getPolyLineForBezier(bezierPoints, 1.0);
   }

   public static Point2d[] getPolyLineForBezier(Point2d[] points, double epsilon) {
      List<Point2d> v = new ArrayList<>(64);
      v.add(points[0]);
      internalBezier(v, points, 7, 2.5 * epsilon);
      return v.toArray(new Point2d[0]);
   }

   protected static void internalBezier(List<Point2d> v, Point2d[] points, int maxDepth, double epsilon) {
      if (maxDepth == 0) {
         v.add(points[points.length - 1]);
      } else {
         double maxD2 = 0.0;

         for (int i = 0; i < points.length - 2; i++) {
            double d2x = points[i + 2].getX() - 2.0 * points[i + 1].getX() + points[i].getX();
            double d2y = points[i + 2].getY() - 2.0 * points[i + 1].getY() + points[i].getY();
            d2x = d2x >= 0.0 ? d2x : -d2x;
            d2y = d2y >= 0.0 ? d2y : -d2y;
            if (d2x > maxD2) {
               maxD2 = d2x;
            }

            if (d2y > maxD2) {
               maxD2 = d2y;
            }
         }

         if (maxD2 < epsilon) {
            v.add(points[points.length - 1]);
         } else {
            Point2d[] pA = new Point2d[points.length];
            Point2d[] pB = new Point2d[points.length];
            if (points.length == 3) {
               pA[0] = points[0];
               pA[1] = new Point2d((points[0].getX() + points[1].getX()) / 2.0, (points[0].getY() + points[1].getY()) / 2.0);
               pB[1] = new Point2d((points[1].getX() + points[2].getX()) / 2.0, (points[1].getY() + points[2].getY()) / 2.0);
               pA[2] = new Point2d((pB[1].getX() + pA[1].getX()) / 2.0, (pB[1].getY() + pA[1].getY()) / 2.0);
               pB[0] = pA[2];
               pB[2] = points[2];
               internalBezier(v, pA, maxDepth - 1, epsilon);
               internalBezier(v, pB, maxDepth - 1, epsilon);
            } else {
               System.err.println("drawBezier fuer diesen Kurvengrad noch nicht implementiert!");
            }
         }
      }
   }

   public static void drawBezier(PixelPlate pixelPlate, Point2d p0, Point2d p1, Point2d p2) {
      Point2d[] bezPoints = new Point2d[]{p0, p1, p2};
      Point2d[] polyLine = getPolyLineForBezier(bezPoints, 0.15);

      for (int i = 0; i < polyLine.length - 1; i++) {
         pixelPlate.drawLine(polyLine[i].getX(), polyLine[i].getY(), polyLine[i + 1].getX(), polyLine[i + 1].getY());
      }
   }
}
