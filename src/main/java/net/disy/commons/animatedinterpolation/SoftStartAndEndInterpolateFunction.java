package net.disy.commons.animatedinterpolation;

public class SoftStartAndEndInterpolateFunction implements IInterpolateFunction {
   @Override
   public double getValue(double x) {
      return (1.0 + Math.sin(x * Math.PI - (Math.PI / 2))) / 2.0;
   }
}
