package de.jave.jave.tool.fill;

import de.jave.jave.algorithm.fill.FillMatchMode;
import de.jave.jave.algorithm.fill.FillMode;
import de.jave.jave.algorithm.fill.GradientStyle;
import de.jave.jave.pattern.Pattern;
import net.disy.commons.core.util.Ensure;

public class FillOptions {
   private char[] gradient = new char[]{' '};
   private FillMatchMode matchMode = FillMatchMode.EQUAL_CHARACTER;
   private FillMode fillMode = FillMode.SOLID;
   private Pattern pattern;
   private GradientStyle gradientStyle = GradientStyle.values()[0];
   private boolean dither = true;

   public FillOptions(Pattern pattern) {
      Ensure.ensureArgumentNotNull(pattern);
      this.pattern = pattern;
   }

   public char[] getGradient(boolean invert) {
      if (!invert) {
         return this.gradient;
      } else {
         char[] result = new char[this.gradient.length];

         for (int i = 0; i < this.gradient.length; i++) {
            result[i] = this.gradient[this.gradient.length - i - 1];
         }

         return result;
      }
   }

   public void setGradient(char[] gradient) {
      Ensure.ensureArgumentNotNull(gradient);
      this.gradient = gradient;
   }

   public FillMatchMode getMatchMode() {
      return this.matchMode;
   }

   public void setMatchMode(FillMatchMode matchMode) {
      Ensure.ensureArgumentNotNull(matchMode);
      this.matchMode = matchMode;
   }

   public FillMode getFillMode() {
      return this.fillMode;
   }

   public void setFillMode(FillMode fillMode) {
      Ensure.ensureArgumentNotNull(fillMode);
      this.fillMode = fillMode;
   }

   public Pattern getPattern() {
      return this.pattern;
   }

   public void setPattern(Pattern pattern) {
      Ensure.ensureArgumentNotNull(pattern);
      this.pattern = pattern;
   }

   public void setGradientStyle(GradientStyle gradientStyle) {
      Ensure.ensureArgumentNotNull(gradientStyle);
      this.gradientStyle = gradientStyle;
   }

   public GradientStyle getGradientStyle() {
      return this.gradientStyle;
   }

   public boolean isDither() {
      return this.dither;
   }

   public void setDither(boolean dither) {
      this.dither = dither;
   }
}
