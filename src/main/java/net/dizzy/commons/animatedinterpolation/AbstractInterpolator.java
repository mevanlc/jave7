package net.dizzy.commons.animatedinterpolation;

public abstract class AbstractInterpolator<T> {
   public abstract T interpolate(T start, T end, double t);

   protected int interpolate(int start, int end, double t) {
      return (int) Math.round(start + (end - start) * t);
   }
}
