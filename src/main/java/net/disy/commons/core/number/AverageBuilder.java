package net.disy.commons.core.number;

public class AverageBuilder {
   private double average = 0.0;
   private int valueCount = 0;

   public void add(Number number) {
      if (number != null) {
         this.add(number.doubleValue());
      }
   }

   public void add(double value) {
      this.valueCount++;
      this.average = (this.average * (double)(this.valueCount - 1) + value) / (double)this.valueCount;
   }

   public Number getAsNumber() {
      return this.valueCount == 0 ? null : this.average;
   }

   public double getAsDouble() {
      return this.average;
   }
}
