package net.disy.commons.swing.geometry;

import java.awt.Point;
import java.awt.Rectangle;

public class SmartPoint extends Point {
   public SmartPoint(int x, int y) {
      super(x, y);
   }

   public SmartPoint(Point point) {
      super(point);
   }

   public Point getVectorTo(Point point) {
      return new Point(point.x - this.x, point.y - this.y);
   }

   public void limitTo(Rectangle limits) {
      this.x = Math.min(Math.max(this.x, limits.x), limits.x + limits.width);
      this.y = Math.min(Math.max(this.y, limits.y), limits.y + limits.height);
   }

   public void subtract(Point subtrahend) {
      this.x = this.x - subtrahend.x;
      this.y = this.y - subtrahend.y;
   }
}
