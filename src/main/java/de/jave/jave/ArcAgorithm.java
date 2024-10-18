package de.jave.jave;

import java.awt.Point;

public class ArcAgorithm {
   public static void drawCircle(Point p1, Point p2, Point p3) {
      drawCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
   }

   public static void drawCircle(double x1, double y1, double x2, double y2, double x3, double y3) {
      if ((x1 != x3 || y1 != y3) && (x2 != x3 || y2 != y3) && (x1 != x2 || y1 != y2)) {
         double p1x = (x1 + x3) / 2.0;
         double p1y = (y1 + y3) / 2.0;
         double p2x = (x2 + x3) / 2.0;
         double p2y = (y2 + y3) / 2.0;
         double n1x = -(y1 - p1y);
         double n1y = x1 - p1x;
         double n2x = -(y2 - p2y);
         double n2y = x2 - p2x;
         double s1 = Math.sqrt(n1x * n1x + n1y * n1y);
         n1x /= s1;
         n1y /= s1;
         double s2 = Math.sqrt(n2x * n2x + n2y * n2y);
         n2x /= s2;
         n2y /= s2;
         if (n1x == n2x && n1y == n2y) {
            System.err.println("Die 3 Punkte sind kolinear!");
         } else {
            double a = (n2x * (p1y - p2y) - (p1x - p2x) * n2y) / (n1x * n2y - n2x * n1y);
            double x0 = p1x + a * n1x;
            double y0 = p1y + a * n1y;
            double r = Math.sqrt(Math.pow(x1 - x0, 2.0) + Math.pow(y1 - y0, 2.0));
            System.err.println("Center: " + x0 + "," + y0 + " Radius: " + r);
         }
      } else {
         System.err.println("mind. 2 der 3 Punkte sind identisch!");
      }
   }

   public static Point2d getCenter(Point p1, Point p2, Point p3) {
      return getCenter(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
   }

   public static Point2d getCenter(Point2d p1, Point2d p2, Point2d p3) {
      return getCenter(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
   }

   public static Point2d getCenter(double x1, double y1, double x2, double y2, double x3, double y3) {
      if ((x1 != x3 || y1 != y3) && (x2 != x3 || y2 != y3) && (x1 != x2 || y1 != y2)) {
         double p1x = (x1 + x3) / 2.0;
         double p1y = (y1 + y3) / 2.0;
         double p2x = (x2 + x3) / 2.0;
         double p2y = (y2 + y3) / 2.0;
         double n1x = -(y1 - p1y);
         double n1y = x1 - p1x;
         double n2x = -(y2 - p2y);
         double n2y = x2 - p2x;
         double s1 = Math.sqrt(n1x * n1x + n1y * n1y);
         n1x /= s1;
         n1y /= s1;
         double s2 = Math.sqrt(n2x * n2x + n2y * n2y);
         n2x /= s2;
         n2y /= s2;
         if (n1x * n2y == n2x * n1y) {
            return null;
         } else {
            double a = (n2x * (p1y - p2y) - (p1x - p2x) * n2y) / (n1x * n2y - n2x * n1y);
            double x0 = p1x + a * n1x;
            double y0 = p1y + a * n1y;
            return new Point2d(x0, y0);
         }
      } else {
         return null;
      }
   }

   public static void main(String[] args) {
      drawCircle(0.0, 2.5, 2.0, 1.5, 2.0, -1.5);
   }
}
