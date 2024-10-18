package net.disy.commons.animatedinterpolation;

public abstract class AbstractInterpolator<T> implements IInterpolator<T> {
   protected final float interpolate(float startValue, float endValue, double t) {
      return (float)((double)startValue + (double)(endValue - startValue) * t);
   }

   protected final double interpolate(double startValue, double endValue, double t) {
      return startValue + (endValue - startValue) * t;
   }

   protected final int interpolate(int startValue, int endValue, double t) {
      return (int)Math.round((double)startValue + (double)(endValue - startValue) * t);
   }
}
