package net.disy.commons.core.number;

public class MinMaxDoubleValueBuilder implements IDoubleValueAddable {
   private double minValue = Double.NaN;
   private double maxValue = Double.NaN;

   public double getMaximum(double fallBackValue) {
      return Double.isNaN(this.maxValue) ? fallBackValue : this.maxValue;
   }

   public double getMinimum(double fallBackValue) {
      return Double.isNaN(this.minValue) ? fallBackValue : this.minValue;
   }

   @Override
   public void add(double value) {
      this.minValue = !(value < this.minValue) && !Double.isNaN(this.minValue) ? this.minValue : value;
      this.maxValue = !(value > this.maxValue) && !Double.isNaN(this.maxValue) ? this.maxValue : value;
   }
}
