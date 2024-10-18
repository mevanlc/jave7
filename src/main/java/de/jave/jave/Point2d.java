package de.jave.jave;

import java.awt.Point;

public class Point2d implements Cloneable {
   private static final double EPSILON = 0.001;
   private static final double _EPSILON = 1000.0;
   private double x;
   private double y;

   public Point2d() {
      this(0.0, 0.0);
   }

   public Point2d(int x, int y) {
      this(x, (double)y);
   }

   public Point2d(Point p) {
      this(p.x, p.y);
   }

   public Point2d(Point2d p) {
      this(p.x, p.y);
   }

   public Point2d(double x, double y) {
      this.x = x;
      this.y = y;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public void translate(double dx, double dy) {
      this.x += dx;
      this.y += dy;
   }

   public void translate(Point2d d) {
      this.x = this.x + d.x;
      this.y = this.y + d.y;
   }

   public void moveTo(double x, double y) {
      this.x = x;
      this.y = y;
   }

   public void moveTo(Point2d d) {
      this.x = d.x;
      this.y = d.y;
   }

   @Override
   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }

   void clone(Point2d p) {
      this.x = p.x;
      this.y = p.y;
   }

   @Override
   public boolean equals(Object other) {
      return !(other instanceof Point2d)
         ? false
         : this.x - 0.001 <= ((Point2d)other).x
            && this.x + 0.001 >= ((Point2d)other).x
            && this.y - 0.001 <= ((Point2d)other).y
            && this.y + 0.001 >= ((Point2d)other).y;
   }

   @Override
   public int hashCode() {
      return ((int)(this.x * 1000.0) << 8) + ((int)(this.y * 1000.0) << 16);
   }

   public double getDistance(Point2d other) {
      return Math.sqrt(Math.pow(this.x - other.x, 2.0) + Math.pow(this.y - other.y, 2.0));
   }

   public double getDistanceTo(Point2d p1, Point2d p2) {
      if (p1.x == p2.x && p1.y == p2.y) {
         return this.getDistance(p1);
      } else {
         double bx = this.x - p1.x;
         double by = this.y - p1.y;
         double ax = p2.x - p1.x;
         double ay = p2.y - p1.y;
         double f = (ax * bx + ay * by) / (ax * ax + ay * ay);
         double qx = p1.x + f * ax;
         double qy = p1.y + f * ay;
         return Math.sqrt(Math.pow(this.x - qx, 2.0) + Math.pow(this.y - qy, 2.0));
      }
   }

   public void normalize() {
      double scale = Math.sqrt(this.x * this.x + this.y * this.y);
      this.x /= scale;
      this.y /= scale;
   }

   public static Point2d getVector(Point2d p1, Point2d p2) {
      return new Point2d(p2.x - p1.x, p2.y - p1.y);
   }

   public double getAngle() {
      if (this.x == 0.0) {
         return this.y > 0.0 ? 0.0 : Math.PI;
      } else {
         double alpha = Math.atan(this.y / this.x);
         if (this.x > 0.0) {
            alpha++;
         } else {
            alpha += Math.PI * 3.0 / 2.0;
         }

         return alpha;
      }
   }

   public void rotate(double alpha) {
      double cosA = Math.cos(alpha);
      double sinA = Math.sin(alpha);
      double x2 = this.x * cosA - this.y * sinA;
      double y2 = this.x * sinA + this.y * cosA;
      this.x = x2;
      this.y = y2;
   }

   public void scale(double d) {
      this.x *= d;
      this.y *= d;
   }

   public void print() {
      System.out.print(this.toString());
   }

   public void println() {
      System.out.println(this.toString());
   }

   @Override
   public String toString() {
      return "Point2d(" + this.x + "," + this.y + ")";
   }
}
