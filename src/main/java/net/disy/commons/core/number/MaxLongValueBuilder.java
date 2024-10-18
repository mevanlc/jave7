package net.disy.commons.core.number;

public class MaxLongValueBuilder {
   private long maxValue;

   public MaxLongValueBuilder(long startValue) {
      this.maxValue = startValue;
   }

   public long getMaximum() {
      return this.maxValue;
   }

   public void add(long value) {
      this.maxValue = value > this.maxValue ? value : this.maxValue;
   }
}
