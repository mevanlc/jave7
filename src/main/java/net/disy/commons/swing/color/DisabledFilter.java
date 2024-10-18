package net.disy.commons.swing.color;

import java.awt.image.RGBImageFilter;

public class DisabledFilter extends RGBImageFilter {
   private final int redAddend;
   private final double redDividend;
   private final int greenAddend;
   private final double greenDividend;
   private final int blueAddend;
   private final double blueDividend;

   public DisabledFilter() {
      this(128, 2.0);
   }

   public DisabledFilter(int addend, double dividend) {
      this(addend, dividend, addend, dividend, addend, dividend);
   }

   public DisabledFilter(int redAddend, double redDividend, int greenAddend, double greenDividend, int blueAddend, double blueDividend) {
      this.redAddend = redAddend;
      this.redDividend = redDividend;
      this.greenAddend = greenAddend;
      this.greenDividend = greenDividend;
      this.blueAddend = blueAddend;
      this.blueDividend = blueDividend;
   }

   @Override
   public int filterRGB(int x, int y, int rgb) {
      int r = (int)((double)(((rgb & 0xFF0000) >> 16) + this.redAddend) / this.redDividend);
      int g = (int)((double)(((rgb & 0xFF00) >> 8) + this.greenAddend) / this.greenDividend);
      int b = (int)((double)((rgb & 0xFF) + this.blueAddend) / this.blueDividend);
      return rgb & 0xFF000000 | r << 16 | g << 8 | b;
   }
}
