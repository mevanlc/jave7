package de.jave.jave.algorithm.fill;

import java.awt.Rectangle;

public class Bounds {
   private int minX;
   private int maxX;
   private int minY;
   private int maxY;

   public Bounds(int x, int y) {
      this.minX = x;
      this.maxX = x;
      this.minY = y;
      this.maxY = y;
   }

   public void add(int x, int y) {
      if (x > this.maxX) {
         this.maxX = x;
      }

      if (y > this.maxY) {
         this.maxY = y;
      }

      if (x < this.minX) {
         this.minX = x;
      }

      if (y < this.minY) {
         this.minY = y;
      }
   }

   public Rectangle getRectangle() {
      return new Rectangle(this.minX, this.minY, this.maxX - this.minX + 1, this.maxY - this.minY + 1);
   }
}
