package de.jave.jave.algorithm.camel;

public class CamelizeImageOptions {
   private double brightness = 0.0;
   private boolean negative = false;

   public double getBrightness() {
      return this.brightness;
   }

   public void setBrightness(double brightness) {
      this.brightness = brightness;
   }

   public boolean isNegative() {
      return this.negative;
   }

   public void setNegative(boolean negative) {
      this.negative = negative;
   }
}
