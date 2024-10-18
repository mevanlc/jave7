package de.jave.image2ascii.algorithm.kicad;

public class KiCadLineRun {
   private final int x;
   private final int y;
   private int width = 1;

   public KiCadLineRun(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public void addSegment() {
      this.width++;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getWidth() {
      return this.width;
   }
}
