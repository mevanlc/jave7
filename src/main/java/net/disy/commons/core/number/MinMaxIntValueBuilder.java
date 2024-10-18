package net.disy.commons.core.number;

@Deprecated
public class MinMaxIntValueBuilder {
   private double minValue = Double.NaN;
   private double maxValue = Double.NaN;

   public int getMaximum() {
      if (!this.hasValues()) {
         throw new IllegalStateException("No max value defined.");
      } else {
         return (int)this.maxValue;
      }
   }

   public int getMaximum(int fallBackValue) {
      return !this.hasValues() ? fallBackValue : (int)this.maxValue;
   }

   public int getMinimum() {
      if (!this.hasValues()) {
         throw new IllegalStateException("No min value defined.");
      } else {
         return (int)this.minValue;
      }
   }

   public int getMinimum(int fallBackValue) {
      return !this.hasValues() ? fallBackValue : (int)this.minValue;
   }

   public boolean hasValues() {
      return !Double.isNaN(this.maxValue);
   }

   public void add(int value) {
      this.minValue = !((double)value < this.minValue) && !Double.isNaN(this.minValue) ? this.minValue : (double)value;
      this.maxValue = !((double)value > this.maxValue) && !Double.isNaN(this.maxValue) ? this.maxValue : (double)value;
   }
}
