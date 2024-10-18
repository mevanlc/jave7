package de.jave.jave.ascii3d;

public class Polygon2d {
   private final double[] xs;
   private final double[] ys;
   private double minX;
   private double minY;
   private double maxX;
   private double maxY;

   public Polygon2d(double[] xs, double[] ys) {
      this.xs = xs;
      this.ys = ys;
      this.minX = xs[0];
      this.maxX = xs[0];
      this.minY = ys[0];
      this.maxY = ys[0];

      for (int i = 1; i < xs.length; i++) {
         if (xs[i] < this.minX) {
            this.minX = xs[i];
         } else if (xs[i] > this.maxX) {
            this.maxX = xs[i];
         }

         if (ys[i] < this.minY) {
            this.minY = ys[i];
         } else if (ys[i] > this.maxY) {
            this.maxY = ys[i];
         }
      }
   }

   public boolean contains(double x, double y) {
      if (!(x < this.minX) && !(x > this.maxX) && !(y < this.minY) && !(y > this.maxY)) {
         int hits = 0;
         int npoints = this.xs.length;
         double lastx = this.xs[npoints - 1];
         double lasty = this.ys[npoints - 1];

         for (int i = 0; i < npoints; i++) {
            double curx;
            double cury;
            curx = this.xs[i];
            cury = this.ys[i];
            label67:
            if (cury != lasty) {
               double leftx;
               if (curx < lastx) {
                  if (x >= lastx) {
                     break label67;
                  }

                  leftx = curx;
               } else {
                  if (x >= curx) {
                     break label67;
                  }

                  leftx = lastx;
               }

               double test1;
               double test2;
               if (cury < lasty) {
                  if (y < cury || y >= lasty) {
                     break label67;
                  }

                  if (x < leftx) {
                     hits++;
                     break label67;
                  }

                  test1 = x - curx;
                  test2 = y - cury;
               } else {
                  if (y < lasty || y >= cury) {
                     break label67;
                  }

                  if (x < leftx) {
                     hits++;
                     break label67;
                  }

                  test1 = x - lastx;
                  test2 = y - lasty;
               }

               if (test1 < test2 / (lasty - cury) * (lastx - curx)) {
                  hits++;
               }
            }

            lastx = curx;
            lasty = cury;
         }

         return (hits & 1) != 0;
      } else {
         return false;
      }
   }

   public double getMinY() {
      return this.minY;
   }

   public double getMaxY() {
      return this.maxY;
   }

   public double getMinX() {
      return this.minX;
   }

   public double getMaxX() {
      return this.maxX;
   }
}
