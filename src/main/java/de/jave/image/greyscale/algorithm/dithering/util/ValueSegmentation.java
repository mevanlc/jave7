package de.jave.image.greyscale.algorithm.dithering.util;

public class ValueSegmentation {
   private int depth;
   private int max;
   private int min;

   public ValueSegmentation(int min, int max, int depth) {
      this.min = min;
      this.max = max;
      this.depth = depth;
   }

   public int getSegment(int value) {
      if (value > this.max) {
         return this.depth - 1;
      } else {
         return value < this.min ? 0 : (value - this.min - 1) * this.depth / (this.max - this.min);
      }
   }

   public int getSegmentValue(int value) {
      return this.getValueForSegment(this.getSegment(value));
   }

   public int getValueForSegment(int segment) {
      return segment * (this.max - this.min) / (this.depth - 1) + this.min;
   }
}
