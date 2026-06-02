package de.jave.jave.algorithm.gradient;

import net.dizzy.commons.core.util.Ensure;

public class AsciiGradientConfiguration {
   private final String[] gradients;

   public AsciiGradientConfiguration(String[] gradients) {
      Ensure.ensureArgumentNotNull(gradients);
      this.gradients = gradients;
   }

   public String getDefaultGradient() {
      return this.gradients[0];
   }

   public String[] getGradients() {
      return this.gradients;
   }
}
