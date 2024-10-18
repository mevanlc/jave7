package net.disy.commons.core.number;

public class MaxIntegerValueBuilder {
   private int maxValue;

   public MaxIntegerValueBuilder(int startValue) {
      this.maxValue = startValue;
   }

   public int getMaximum() {
      return this.maxValue;
   }

   public void add(int value) {
      this.maxValue = value > this.maxValue ? value : this.maxValue;
   }
}
