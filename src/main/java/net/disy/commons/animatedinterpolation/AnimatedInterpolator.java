package net.disy.commons.animatedinterpolation;

import net.disy.commons.core.util.Ensure;

public class AnimatedInterpolator<T> {
   private final IInterpolator<T> interpolator;
   private final IInterpolatedStepExecuter<T> executer;
   private final IInterpolateFunction function;
   private T currentValue;
   private T endValue;
   private T startValue;
   private long animationStartTimeMillis;
   private long animationEndTimeMillis;
   private boolean animationInAction = false;

   public AnimatedInterpolator(IInterpolator<T> interpolator, IInterpolatedStepExecuter<T> executer, IInterpolateFunction function) {
      Ensure.ensureArgumentNotNull(interpolator);
      Ensure.ensureArgumentNotNull(executer);
      Ensure.ensureArgumentNotNull(function);
      this.interpolator = interpolator;
      this.executer = executer;
      this.function = function;
   }

   public synchronized void animationStepFinished() {
      this.performNextStep();
   }

   public synchronized void startAnimation(T newStartValue, T newEndValue, int durationMillis) {
      Ensure.ensureArgumentNotNull(newStartValue);
      Ensure.ensureArgumentNotNull(newEndValue);
      this.animationInAction = true;
      this.endValue = newEndValue;
      this.startValue = this.currentValue == null ? newStartValue : this.currentValue;
      this.animationStartTimeMillis = System.currentTimeMillis();
      this.animationEndTimeMillis = this.animationStartTimeMillis + (long)durationMillis;
      this.performNextStep();
   }

   public synchronized void reset() {
      this.animationInAction = false;
      this.currentValue = null;
   }

   private void performNextStep() {
      if (this.animationInAction) {
         try {
            Thread.sleep(50L);
         } catch (InterruptedException var7) {
         }

         long currentTimeMillis = System.currentTimeMillis();
         if (currentTimeMillis >= this.animationEndTimeMillis) {
            this.animationInAction = false;
            this.currentValue = this.endValue;
            this.executer.executeInterpolatedStep(this.endValue, false);
         } else {
            double linearT = ((double)currentTimeMillis - (double)this.animationStartTimeMillis)
               / (double)(this.animationEndTimeMillis - this.animationStartTimeMillis);
            double t = this.function.getValue(linearT);
            this.currentValue = this.interpolator.interpolate(this.startValue, this.endValue, t);
            this.executer.executeInterpolatedStep(this.currentValue, true);
         }
      }
   }
}
