package de.jave.jave.tool.auxiliarylines;

import de.jave.jave.Point2d;

public class Line2d {
   private final Point2d startPoint;
   private Point2d endPoint;

   public Line2d(Point2d p0, Point2d p1) {
      this.startPoint = p0;
      this.endPoint = p1;
   }

   public double length() {
      return this.startPoint.getDistance(this.endPoint);
   }

   public void translate(double dx, double dy) {
      this.startPoint.translate(dx, dy);
      this.endPoint.translate(dx, dy);
   }

   public Point2d getStartPoint() {
      return this.startPoint;
   }

   public Point2d getEndPoint() {
      return this.endPoint;
   }

   public void translate(Point2d d) {
      this.startPoint.translate(d);
      this.endPoint.translate(d);
   }

   public void setEndPoint(Point2d endPoint) {
      this.endPoint = endPoint;
   }
}
